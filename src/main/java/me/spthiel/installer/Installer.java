package me.spthiel.installer;

import javax.swing.*;
import java.awt.Button;
import java.awt.TextField;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class Installer extends JFrame implements ActionListener {
	
	private final TextField      path;
	private       Consumer<File> consumer;
	
	public Installer(String moduleName) {
		super(moduleName + "me/spthiel/installer");
		
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setResizable(false);
		
		Button selectPath = new Button("INSTALL TO");
		selectPath.setBounds(10, 10, 110, 30);
		File file = new File(OSValidator.getOS().getBasePath());
		path = new TextField(file.getAbsolutePath());
		path.setBounds(130, 10, 320, 30);
		Button confirm = new Button("CONFIRM");
		confirm.setBounds(175, 50, 110, 30);
		
		confirm.addActionListener((e) -> {
			try {
				File startFile = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
				File targetFile = new File(path.getText() + String.join(File.separatorChar + "", new String[]{"","liteconfig","common","macros","modules"}), startFile.getName());
				
				if (targetFile.getParentFile().exists()) {
					File parent = targetFile.getParentFile();
					Pattern pattern = Pattern.compile("module_" + Pattern.quote(moduleName.toLowerCase()) + "[-_].+?\\.jar", Pattern.CASE_INSENSITIVE);
					for(File moduleFile : Objects.requireNonNull(parent.listFiles())) {
						if (pattern.matcher(moduleFile.getName()).find()) {
							moduleFile.delete();
						}
					}
					
					Files.copy(startFile.toPath(), targetFile.toPath());
					startFile.deleteOnExit();
				}
				
			} catch (URISyntaxException | IOException exception) {
				exception.printStackTrace();
			}
			this.setVisible(false);
			System.exit(0);
		});
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setPreferredSize(new Dimension(460, 90));
		panel.add(selectPath);
		panel.add(path);
		panel.add(confirm);
		panel.setBackground(Color.black);
		
		JFrame frame = this;
		
		selectPath.addActionListener(this);
		
		this.add(panel);
		this.pack();
		this.setVisible(true);
		
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(dimension.width/2 - getWidth()/2, dimension.height/2 - getHeight()/2, getWidth(), getHeight());
		
	}
	private static void copyFileUsingStream(File source, File dest) throws IOException {
		InputStream  is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(source);
			os = new FileOutputStream(dest);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		} finally {
			is.close();
			os.close();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException classNotFoundException) {
			classNotFoundException.printStackTrace();
		}
		Boolean old = UIManager.getBoolean("FileChooser.readOnly");
		UIManager.put("FileChooser.readOnly", Boolean.TRUE);
		JFileChooser chooser = new JFileChooser();
		UIManager.put("FileChooser.readOnly", old);
		chooser.setCurrentDirectory(new File(path.getText()));
		chooser.setDialogTitle("Select minecraft directory");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			this.path.setText(chooser.getSelectedFile().toString());
		}
	}
	
	public void then(Consumer<File> consumer) {
		this.consumer = consumer;
	}
	
	static class FolderFilter implements FilenameFilter
	{
		public boolean accept(File dir, String name)
		{
			return new File(dir,name).isDirectory();
		}
	}
}
