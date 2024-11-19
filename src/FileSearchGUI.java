import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileSearchGUI {
    private JFrame frame;
    private JTextArea originalTextArea;
    private JTextArea filteredTextArea;
    private JTextField searchField;
    private Path filePath;

    public FileSearchGUI() {
        // Create the main frame
        frame = new JFrame("File Search Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Layout setup
        frame.setLayout(new BorderLayout());

        // Create panels for text areas and search field
        JPanel textPanel = new JPanel(new GridLayout(1, 2));
        JPanel controlPanel = new JPanel(new FlowLayout());

        // Create and set up the text areas
        originalTextArea = new JTextArea();
        filteredTextArea = new JTextArea();
        originalTextArea.setEditable(false);
        filteredTextArea.setEditable(false);

        // Add scroll panes for the text areas
        textPanel.add(new JScrollPane(originalTextArea));
        textPanel.add(new JScrollPane(filteredTextArea));

        // Create the search field
        searchField = new JTextField(20);

        // Create buttons
        JButton loadButton = new JButton("Load File");
        JButton searchButton = new JButton("Search");
        JButton quitButton = new JButton("Quit");

        // Add components to the control panel
        controlPanel.add(new JLabel("Search:"));
        controlPanel.add(searchField);
        controlPanel.add(loadButton);
        controlPanel.add(searchButton);
        controlPanel.add(quitButton);

        // Add panels to the frame
        frame.add(textPanel, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);

        // Add action listeners for buttons
        loadButton.addActionListener(e -> loadFile());
        searchButton.addActionListener(e -> searchFile());
        quitButton.addActionListener(e -> System.exit(0));

        // Make the frame visible
        frame.setVisible(true);
    }

    private void loadFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            filePath = fileChooser.getSelectedFile().toPath();
            try (Stream<String> lines = Files.lines(filePath)) {
                originalTextArea.setText(lines.collect(Collectors.joining("\n")));
                filteredTextArea.setText(""); // Clear the filtered area
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error loading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void searchFile() {
        if (filePath == null) {
            JOptionPane.showMessageDialog(frame, "No file loaded. Please load a file first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String searchString = searchField.getText().trim();
        if (searchString.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter a search string.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Stream<String> lines = Files.lines(filePath)) {
            String filteredContent = lines.filter(line -> line.contains(searchString))
                    .collect(Collectors.joining("\n"));
            filteredTextArea.setText(filteredContent.isEmpty() ? "No matches found." : filteredContent);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error searching file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FileSearchGUI::new);
    }
}
