package com.skillbox.boxes.object_methods;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.skillbox.boxes.object_methods.geometry.Circle;

class BinaryToTextSystemOutputStream extends OutputStream {

  private long mPosition;

  public BinaryToTextSystemOutputStream() {
    super();
    System.out.println("\nBinaryToTextSystemOutputStream starting");
    mPosition = 0L;
  }

  @Override
  public void close() throws IOException {
    super.close();
    System.out.println("\nBinaryToTextSystemOutputStream complete");
  }

  @Override
  public void write(int ch) throws IOException {
    if (ch >= 32 && ch < 127) {
      System.out.format("  %s", (char) ch);
    } else {
      System.out.format(" %02X", ch);
    }
    mPosition++;
    if ((mPosition % 40) == 0) {
      System.out.print('\n');
    }
  }

  static public void dumpFile(File file) throws IOException {

    BinaryToTextSystemOutputStream out = null;

    try {
      out = new BinaryToTextSystemOutputStream();

      BufferedInputStream in = null;

      try {
        in = new BufferedInputStream(new FileInputStream(file));

        for (int data = in.read(); data != -1; data = in.read()) {
          out.write(data);
        }
      } finally {
        if (in != null) {
          in.close();
        }
      }
    } finally {
      if (out != null) {
        out.close();
      }
    }
  }
}

public class App {
  private static final String APP_VERSION_STRING = "Circlesv1.0";

  public static CommandLine getCommandLine(String[] args, Options options) {
    // create the parser
    CommandLineParser parser = new BasicParser();
    try {
      // parse the command line arguments
      return parser.parse(options, args);
    } catch (ParseException exp) {
      // oops, something went wrong
      System.err.println("Parsing failed.  Reason: " + exp.getMessage());
    }

    return null;
  }

  @SuppressWarnings("static-access")
  private static Options buildCommandLineOptions() {
    Options options = new Options();

    Option outputfile = OptionBuilder.withArgName("output").hasArg()
        .withType(File.class).withDescription("save to file").create("file");
    options.addOption(outputfile);

    Option circleCount = OptionBuilder.withArgName("count").hasArg()
        .withType(Number.class).withDescription("number of circles")
        .create("count");
    options.addOption(circleCount);
    return options;
  }

  public static void main(String[] args) {
    Options options = buildCommandLineOptions();
    CommandLine commandLine = getCommandLine(args, options);
    try {
      int iCircleCount = 5;

      if (commandLine.hasOption("count")) {
        iCircleCount = Integer.parseInt(commandLine.getOptionValue("count"));
      }

      if (iCircleCount > 0) {
        try {
          File outputFile = File.createTempFile("circles", ".ser", null);

          System.out.printf("Filename:" + outputFile.getAbsolutePath());

          writeRandomCircles(iCircleCount, outputFile);

          dumpBinaryFile(outputFile);

          ArrayList<Circle> circles = readCircles(outputFile);

          System.out.println(String.format("%d Circles Read From: %s",
              circles.size(), outputFile.getAbsolutePath()));
          for (Circle c : circles) {
            System.out.println(c.toString());
          }

        } catch (IOException e) {
          System.err.println("IOException: " + e.getMessage());
          e.printStackTrace();
        } catch (ClassNotFoundException e) {
          System.err.println("ClassNotFoundException: " + e.getMessage());
          e.printStackTrace();
        }
      }
    } catch (NumberFormatException nfe) {
      // automatically generate the help statement
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("circles", options);
    }
  }

  private static ArrayList<Circle> readCircles(File file) throws IOException,
      ClassNotFoundException {
    ArrayList<Circle> circles = new ArrayList<Circle>();

    FileInputStream fis = null;

    try {
      fis = new FileInputStream(file);
      ObjectInputStream ois = null;

      try {
        ois = new ObjectInputStream(fis);

        Object oVersion = ois.readObject();
        assert (APP_VERSION_STRING == oVersion);
        Object oCount = ois.readObject();
        assert (oCount instanceof Integer);

        Integer count = (Integer) oCount;

        for (int i = 0; i < count; i++) {
          Object object = ois.readObject();
          if (object instanceof Circle) {
            circles.add((Circle) object);
          }
        }
      } finally {
        if (null != ois) {
          ois.close();
        }
      }

    } finally {
      if (null != fis) {
        fis.close();
      }
    }

    return circles;
  }

  private static void dumpBinaryFile(File file) throws IOException {
    BinaryToTextSystemOutputStream.dumpFile(file);
  }

  private static void writeRandomCircles(int iCircleCount, File outputFile)
      throws FileNotFoundException, IOException {
    FileOutputStream fos = new FileOutputStream(outputFile);

    try {
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      try {
        oos.writeObject(APP_VERSION_STRING);
        oos.writeObject(iCircleCount);

        for (int i = 0; i < iCircleCount; i++) {

          int x = randomInt(-10, 10);
          int y = randomInt(-10, 10);
          int radius = randomInt(-10, 10);

          Circle circle = new Circle(x, y, radius);
          circle.setName(String.format("Shape#%d", i + 1));
          circle.setMemo(String.format("I'm %s", circle.getName()));
          System.out.println("Writing :" + circle.toString());
          oos.writeObject(circle);
        }
      } finally {
        if (null != oos) {
          oos.close();
        }
      }
    } finally {
      if (null != fos) {
        fos.close();
      }
    }
  }

  private static Random sGenerator = new Random(System.currentTimeMillis());

  static int randomInt(int low, int high) {

    if (low == high) {
      return low;
    }

    int range = (high - low) + 1;

    int rand = sGenerator.nextInt(range) + low;

    return rand;
  }
}
