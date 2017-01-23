package gedi.app.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import org.controlsfx.dialog.ExceptionDialog;
import org.controlsfx.tools.ValueExtractor;
import org.controlsfx.validation.ValidationSupport;

import gedi.app.Config;
import gedi.app.classpath.ClassPath;
import gedi.app.classpath.ClassPathCache;
import gedi.app.classpath.DirectoryClassPath;
import gedi.app.classpath.JARClassPath;
import gedi.util.FileUtils;
import gedi.util.RunUtils;
import gedi.util.io.Directory;
import gedi.util.io.text.LineIterator;
import gedi.util.io.text.LineOrientedFile;
import gedi.util.io.text.LineWriter;
import gedi.util.io.text.StreamLineReader;
import gedi.util.io.text.jph.Jhp;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class StartCommandConfigurator implements Configurator<String>{

	public static String name = "starter";
	
	@Override
	public String getSection() {
		return "Gedi";
	}

	@Override
	public String getLabel() {
		return "Gedi runner";
	}

	@Override
	public String getHelp() {
		return "For several use cases, the command gedi must be runnable from command line to start Gedi components.";
	}

	@Override
	public VBox getAdditionalNode(Control parent) {
		VBox re = new VBox(5);
		
		Button bashrc = new Button("Extend PATH variable in .bashrc ("+(ClassPathCache.getInstance().getClassPathOfClass(Configurator.class) instanceof JARClassPath?"JAR":"Devel")+")");
		bashrc.setOnAction(ae->{
			try {
				Path exec = getExecPath();
				String add = "export PATH="+exec.toString()+":$PATH\n";
				
				String lines = new LineOrientedFile(System.getProperty("user.home")+"/.bashrc").readAllText();
				if (!lines.endsWith(add)) {
					LineWriter writer = new LineOrientedFile(System.getProperty("user.home")+"/.bashrc").write();
					writer.writeLine(lines);
					writer.write(add);
					writer.close();
				}
				
				bashrc.setDisable(true);
				((TextField)parent).textProperty().setValue("");
				((TextField)parent).textProperty().setValue("Testing 'gedi -n'");
				
				re.getChildren().clear();
				re.getChildren().add(new Label("Before using gedi, you must open a new shell (or source ~/.bashrc)"));
			} catch (Exception e) {
				new ExceptionDialog(e).showAndWait();
			}
		});
		
		re.getChildren().add(bashrc);
		
		return re;
	}
	
	private Path getExecPath() throws Exception {
		ClassPath cp = ClassPathCache.getInstance().getClassPathOfClass(Configurator.class);
		new File(Config.getInstance().getBinFolder()).mkdirs();
		Path exec = Paths.get(Config.getInstance().getBinFolder()+"/gedi");
		String src = new LineIterator(getClass().getResource("/resources/templates/exec_gedi").openStream()).concat("\n");
		Jhp jhp = new Jhp();
		jhp.getJs().setInterpolateStrings(false);
		
		if (cp instanceof DirectoryClassPath) {
			jhp.getJs().putVariable("type", "devel");
			jhp.getJs().putVariable("root", Paths.get(cp.getURL().toURI()).getParent().getParent());
		}
		else if (cp instanceof JARClassPath) {
			jhp.getJs().putVariable("type", "jar");
			jhp.getJs().putVariable("root", Paths.get(cp.getURL().toURI()).getParent());
		}
		else throw new Exception("Cannot determine classpath!");

		src = jhp.apply(src);
		
		FileUtils.writeAllText(src, exec.toFile());
		exec.toFile().setExecutable(true);
		return exec.getParent();
	}

	@Override
	public Control getControl() {
		TextField tf = new TextField("Testing 'gedi -n'");
		tf.setPrefColumnCount(50);
		tf.setDisable(true);
		return tf;
	}


	private boolean isConfigured() {
		try {
			return new ProcessBuilder().command("bash","-i", "-c", "gedi -n; exit &> /dev/null").start().waitFor()==10;
		} catch (InterruptedException | IOException e) {
			return false;
		}
	}
	
	@Override
	public String validate(String value) {
		return isConfigured()?null:"Cannot call gedi -n!";
	}

	@Override
	public void set(String value) {
	}

}
