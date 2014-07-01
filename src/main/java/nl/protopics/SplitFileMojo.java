package nl.protopics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.Scanner;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.StringUtils;

@Mojo(name = "split", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class SplitFileMojo extends AbstractMojo
{
	/**
	 * Source file. This file will be written to target files specified by targetFilePrefix
	 * and targetFileSuffix, split on lines matching splitExpression
	 */
	@Parameter(required = true)
	private File sourceFile;

	/**
	 * Target filename prefix.
	 * All the created filenames will be formed by targetFilePrefix + number + targetFileSuffix.
	 */
	@Parameter(required = true)
	private String targetFilePrefix;

	/**
	 * Target filename suffix.
	 * All the created filenames will be formed by targetFilePrefix + number + targetFileSuffix.
	 */
	@Parameter(required = true)
	private String targetFileSuffix;

	/**
	 * String to match lines where the sourceFile will be split.
	 * Nothing else should be on the line, apart from whitespace.
	 */
	@Parameter(required = true)
	private String splitExpression;

	private int fileNumber = 0;

	public void execute() throws MojoExecutionException
	{
		if (!sourceFile.exists())
		{
			getLog().error(MessageFormat.format("sourceFile ''{0}'' bestaat niet.", sourceFile.toString()));
		}
		else
		{
			getLog().info(MessageFormat.format("sourceFile ''{0}''", sourceFile.toString()));

			try
			{
				split();
			}
			catch (IOException e)
			{
				getLog().error("Fout bij splitsen: " + e.getMessage(), e);
			}
		}
	}

	private void split() throws IOException
	{
		Scanner scanner = new Scanner(sourceFile);
		PrintStream printer = nextTargetStream();

		while (scanner.hasNext())
		{
			String line = scanner.nextLine();
			if (isSplitter(line))
			{
				printer.close();
				printer = nextTargetStream();
			}
			else
			{
				printer.println(line);
			}
		}
	}

	private boolean isSplitter(String line)
	{
		return splitExpression.equals(StringUtils.trim(line));
	}

	private PrintStream nextTargetStream() throws FileNotFoundException
	{
		fileNumber++;
		File file = new File(targetFilePrefix + fileNumber + targetFileSuffix);
		getLog().info(MessageFormat.format("targetFile ''{0}''", file.toString()));
		return new PrintStream(file);
	}
}
