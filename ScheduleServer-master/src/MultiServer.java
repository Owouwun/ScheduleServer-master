import group.Student;
import group.GroupOperator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MultiServer {
    private ServerSocket serverSocket;
    private static GroupOperator go = new GroupOperator();
    private static String goJSON;
    private final static String filePath = "info.txt";

    public void start(int port) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        try {
            goJSON = readFile(filePath, StandardCharsets.UTF_8);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        go = gson.fromJson(goJSON, GroupOperator.class);

        serverSocket = new ServerSocket(port);
        while (true)
            new EchoClientHandler(serverSocket.accept()).start();
    }

    public void stop() throws IOException {
        serverSocket.close();
    }

    private static class EchoClientHandler extends Thread {
        private final Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public EchoClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            String inputLine = null;
            while (true) {
                try {
                    if ((inputLine = in.readLine()) == null) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (".".equals(inputLine)) {
                    out.println("bye");
                    break;
                }
                if ("{R}".equals(inputLine))
                    out.println(goJSON);
                if (inputLine != null)
                    switch (inputLine.charAt(0)) {
                        case 'd': {
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = gsonBuilder.create();
                            String[] ids = inputLine.substring(1).split(",");
                            int groupID = Integer.parseInt(ids[0]);
                            int subjectID = Integer.parseInt(ids[1]);
                            go.delSubject(groupID, subjectID);
                            goJSON = gson.toJson(go);
                            writeFile(filePath, goJSON);
                            out.println(goJSON);
                            break;
                        }
                        case 'e': {
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = gsonBuilder.create();
                            String[] parts = inputLine.substring(1).split("##");
                            String[] ids = parts[0].split(",");
                            int groupID = Integer.parseInt(ids[0]);
                            int studentID = Integer.parseInt(ids[1]);
                            Student tempStudent = gson.fromJson(parts[1], Student.class);
                            go.editSubject(groupID, studentID, tempStudent);
                            goJSON = gson.toJson(go);
                            writeFile(filePath, goJSON);
                            out.println(goJSON);
                            break;
                        }
                        case 'u': {
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = gsonBuilder.create();
                            GroupOperator tempGo = gson.fromJson(inputLine.substring(1), GroupOperator.class);
                            go.setGroups(tempGo.getGroups());
                            goJSON = gson.toJson(go);
                            writeFile(filePath, goJSON);
                        }
                        case 'a': {
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = gsonBuilder.create();
                            String[] parts = inputLine.substring(1).split("##");
                            Student tempStudent = gson.fromJson(parts[1], Student.class);
                            go.addSubject(parts[0], tempStudent);
                            goJSON = gson.toJson(go);
                            writeFile(filePath, goJSON);
                            out.println(goJSON);
                        }
                    }
            }

            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            out.close();
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public static void writeFile(String path, String text) {
        try (FileWriter writer = new FileWriter(path, false)) {
            writer.write(text);
            writer.flush();
        } catch(IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
