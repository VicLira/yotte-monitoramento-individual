package br.com.sptech.modelo.banco.jdbc.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActiveWindowDetector {

    public List<WindowInfo> getActiveWindowInfo() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            return getWindowsInfo();
        } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
            return getLinuxInfo();
        } else {
            System.out.println("Sistema operacional não suportado.");
            return Collections.emptyList();
        }
    }

    public List<WindowInfo> getWindowsInfo() {
        // Implemente a lógica do Windows aqui, se necessário
        return Collections.emptyList();
    }

    private List<WindowInfo> getLinuxInfo() {
        try {
            List<WindowInfo> windowInfoList = new ArrayList<>();

            // Obtém o nome de usuário do sistema
            String username = System.getProperty("user.name");

            // Obtém informações sobre todas as janelas usando wmctrl
            Process process = Runtime.getRuntime().exec("wmctrl -l -p");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+", 9);

                // Certifica-se de que há pelo menos 7 campos (ID da janela, desktop, PID, nome da aplicação)
                if (parts.length >= 7) {
                    String windowId = parts[0];
                    int pid = Integer.parseInt(parts[2]);
                    String windowName = extractWindowName(parts.length == 7 ? parts[6] : parts[7], username); // O campo do nome da aplicação

                    // Obtém informações de CPU e memória usando ps
                    ProcessPerformanceInfo performanceInfo = getProcessPerformanceInfo(pid);

                    // Adiciona as informações da janela à lista
                    windowInfoList.add(new WindowInfo(windowName, pid, true, performanceInfo));

                    // Adicione logs para ajudar na depuração
                    System.out.println("Window ID: " + windowId);
                    System.out.println("Window Name: " + windowName);
                    System.out.println("PID: " + pid);
                }
            }

            return windowInfoList;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private String extractWindowName(String windowName, String username) {
        // Usar uma expressão regular para encontrar o que vem após o nome do usuário
        String regex = username + "\\s*(.*?)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(windowName);

        if (matcher.find()) {
            // Se a expressão regular encontrar uma correspondência, obter o grupo capturado
            return matcher.group(1).trim();
        } else {
            // Se não houver correspondência, retornar a string original
            return windowName;
        }
    }

    public boolean isPidInForeground(Long processId) {
        try {
            // Obtém informações sobre a área de trabalho ativa usando wmctrl
            Process wmctrlProcess = Runtime.getRuntime().exec("wmctrl -d");
            BufferedReader wmctrlReader = new BufferedReader(new InputStreamReader(wmctrlProcess.getInputStream()));
            String activeDesktopInfo = wmctrlReader.readLine();

            if (activeDesktopInfo != null) {
                // Obtém o número da área de trabalho ativa
                int activeDesktopNumber = Integer.parseInt(activeDesktopInfo.split("\\s+")[0]);

                // Obtém informações sobre todas as janelas na área de trabalho ativa usando wmctrl
                Process process = Runtime.getRuntime().exec("wmctrl -l -p");
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\\s+", 9);

                    // Certifica-se de que há pelo menos 7 campos (ID da janela, desktop, PID, nome da aplicação)
                    if (parts.length >= 7) {
                        String windowId = parts[0];
                        int desktopNumber = Integer.parseInt(parts[1]);
                        int pid = parsePid(parts[2]);
                        String windowName = extractWindowName(parts.length == 7 ? parts[6] : parts[7], null);

                        // Verifica se a janela tem o mesmo PID e está na área de trabalho ativa = está em segundo plano
                        if (pid == processId.intValue() && desktopNumber == activeDesktopNumber) {
                            return false;
                        }
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        // Em caso de erro ou se o PID estiver em primeiro plano
        return true;
    }

    private int parsePid(String pidString) {
        try {
            // Tenta converter a string para um número inteiro
            return Integer.parseInt(pidString);
        } catch (NumberFormatException e) {
            // Em caso de erro, retorna -1 ou outro valor padrão, dependendo do seu caso
            return -1;
        }
    }

    private ProcessPerformanceInfo getProcessPerformanceInfo(int processId) {
        try {
            // Executa o comando top para obter informações de CPU e memória
            Process process = Runtime.getRuntime().exec("top -b -n 1 -p " + processId);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            // Lê as linhas até encontrar a linha com as informações do processo
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith(String.valueOf(processId))) {
                    String[] values = line.trim().split("\\s+");

                    // Verifica se há valores suficientes
                    if (values.length >= 12) {
                        // Substitui vírgula por ponto e converte para double
                        double cpuUsage = Double.parseDouble(values[8].replace(",", "."));
                        double memoryUsage = Double.parseDouble(values[9].replace(",", "."));

                        // Aguarda o término do processo top
                        process.waitFor();

                        // Retorna as informações de desempenho
                        return new ProcessPerformanceInfo(cpuUsage, memoryUsage);
                    }
                }
            }

            // Caso não encontre as informações, retorna null
            return null;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static class WindowInfo {
        private final String windowName;
        private final int processId;
        private final boolean inForeground;
        private final ProcessPerformanceInfo performanceInfo;

        public WindowInfo(String windowName, int processId, boolean inForeground, ProcessPerformanceInfo performanceInfo) {
            this.windowName = windowName;
            this.processId = processId;
            this.inForeground = inForeground;
            this.performanceInfo = performanceInfo;
        }

        public String getWindowName() {
            return windowName;
        }

        public int getProcessId() {
            return processId;
        }

        public boolean isInForeground() {
            return inForeground;
        }

        public ProcessPerformanceInfo getPerformanceInfo() {
            return performanceInfo;
        }

    }

    public static class ProcessPerformanceInfo {
        private final double cpuUsage; // Em percentagem
        private final double memoryUsage; // Em percentagem

        public ProcessPerformanceInfo(double cpuUsage, double memoryUsage) {
            this.cpuUsage = cpuUsage;
            this.memoryUsage = memoryUsage;
        }

        public double getCpuUsage() {
            return cpuUsage;
        }

        public double getMemoryUsage() {
            return memoryUsage;
        }
    }
}
