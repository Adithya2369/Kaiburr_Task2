package kaiburr.task1.service;

import kaiburr.task1.model.*;
import kaiburr.task1.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class TaskService {

    @Autowired
    private TaskRepository repo;

    // SECURITY: Whitelist of commands allowed to be executed
    private static final Set<String> SAFE_COMMANDS = Set.of("ping", "echo", "cd", "date", "time");

    public List<Task> getAll() { return repo.findAll(); }
    public Optional<Task> getById(String id) { return repo.findById(id); }
    public Task add(Task task) { return repo.save(task); }
    public void delete(String id) { repo.deleteById(id); }
    public List<Task> searchByName(String name) { return repo.findByNameContainingIgnoreCase(name); }

    public Task execute(String id) throws Exception {
        
        // Find task or throw NoSuchElementException (will result in HTTP 500)
        Task task = repo.findById(id).orElseThrow(); 

        TaskExecution exec = new TaskExecution();
        exec.setStartTime(new Date());

        String fullCommand = task.getCommand().trim();
        
        // Input validation checks
        if (fullCommand.isEmpty()) {
            throw new IllegalArgumentException("Command cannot be empty.");
        }

        // SECURITY: Command Validation Logic
        // Split the command to get the base executable name
        String baseCommand = fullCommand.split("\\s+")[0].toLowerCase();
        
        // Check if the base command is in the SAFE_COMMANDS set
        if (!SAFE_COMMANDS.contains(baseCommand)) {
            // Throw a security exception if the command is not whitelisted
            throw new SecurityException("Command '" + baseCommand + "' is not a permitted command.");
        }
        
        // EXECUTION: Use ProcessBuilder with cmd /c for robust execution on Windows
        // Arguments: "cmd", "/c", and the full command string
        ProcessBuilder pb = new ProcessBuilder("cmd", "/c", fullCommand);
        
        // Redirect the error stream to the output stream to prevent deadlocks
        pb.redirectErrorStream(true); 
        
        Process p = pb.start(); 

        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        
        // Read the combined output/error stream
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }
        
        // Wait for the process to complete and get the exit code
        int exitCode = p.waitFor(); 

        exec.setEndTime(new Date());
        
        // Append execution details
        output.append("\nProcess exited with code: ").append(exitCode);
        
        exec.setOutput(output.toString());

        // Update task execution list
        if (task.getTaskExecutions() == null) task.setTaskExecutions(new ArrayList<>());
        task.getTaskExecutions().add(exec);
        
        return repo.save(task);
    }
}