package com.vrezerino.negativeharmonyinverter;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.OverlayLayout;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;

public class NegativeHarmonyUI extends JFrame {
    private final String infoText = 
        "This program inverts musical notes within a key center, i.e over an axis.\n"
      + "The point is to discover interesting chords and/or chord progressions.\n\n"

      + "For example, if the key center is C, the axis is dead between C and G, which\n"
      + "means it is actually between Eb and E. Notes are then reflected over this axis.\n"
      + "When the axis is C/G, C becomes G, Db becomes Gb, and so on. You could think of\n"
      + "it as flipping numbers in a number line over zero: -5 becomes 5 etc.\n\n"

      + "Another way to visualize the process is laying the axis on circle of fifths.\n"
      + "Circle of fifths is a way of organizing the 12 chromatic pitches as a sequence of\n"
      + "perfect fifths. The circle diagram shows the number of sharps or flats in each\n"
      + "key signature, with the major key indicated by a capital letter and the minor key\n"
      + "indicated by a lower-case letter. Major and minor keys that have the same key\n"
      + "signature are referred to as relative major and relative minor of one another.\n"
      + "Circle of fifths can be used in composition. The circle of fifths is used to\n"
      + "organize and describe the harmonic function of chords. For example, chords can\n"
      + "progress in a pattern of ascending perfect fourths (alternately viewed as descending\n"
      + "perfect fifths) in \"functional succession\". \n\n"

      + "In this program, the circle of fifths is used so one can easily visualize\n"
      + "how notes are reflected over each axis. The arrows point to notes that become\n"
      + "each other, when reflected/inverted over the axis.\n\n"

      + "(c) 2021 Patrick Park a.k.a Vrezerino a.k.a Vres."
      ;

    private final ChromaticScale chromaticScale = new ChromaticScale();
    private final Font font = new Font("Times New Roman", Font.PLAIN, 14);
    private final Dimension panelDimensions = new Dimension(230, 230);
    private final ImageIcon programIcon = new ImageIcon(getClass().getClassLoader().getResource("programIcon.jpg"));
    private final ImageIcon circleOfFifthsImg = new ImageIcon(getClass().getClassLoader().getResource("cof.png"));
    private final ImageIcon axis = new ImageIcon(getClass().getClassLoader().getResource("axis.png"));
    private final ImageIcon infoIcon = new ImageIcon(getClass().getClassLoader().getResource("infoIcon.png"));
    private int index;
    private double degrees;

    NegativeHarmonyUI() {
        try {
            setUp();
            initComponents();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUp() throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        setTitle("Negative Harmony Inverter");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initComponents() throws Exception {
        JPanel chromaticScaleBtnPanel = new JPanel(new GridLayout(4, 3));
        chromaticScaleBtnPanel.setPreferredSize(panelDimensions);

        // Create chromatic scale keyboard
        chromaticScale.getScale().keySet().forEach((note) -> {
            chromaticScaleBtnPanel.add(new JButton((String) note));
        });


        // Initialize text fields of the top-right portion of the frame
        JTextArea inputNotes = new JTextArea();
        inputNotes.setEditable(false);
        inputNotes.setFont(font);
        inputNotes.setBorder(new TitledBorder("Input"));

        JTextArea invertedNotes = new JTextArea();
        invertedNotes.setEditable(false);
        invertedNotes.setFont(font);
        invertedNotes.setBorder(new TitledBorder("Output"));

        // Create a panel with circle of fifths in it and add rotating axis on top
        JPanel circleOfFifthsPanel = new JPanel();
        circleOfFifthsPanel.setLayout(new OverlayLayout(circleOfFifthsPanel));

        JLabel circleOfFifthsLabel = new JLabel(circleOfFifthsImg);
        JLabel axisLabel = new JLabel(axis);

        circleOfFifthsPanel.add(circleOfFifthsLabel);
        circleOfFifthsPanel.add(axisLabel);

        JSpinner keyCenters = new JSpinner(new SpinnerListModel(chromaticScale.getScale().keySet().toArray()));
        keyCenters.setToolTipText("Choose key center to invert notes within.");

        Arrays.stream(chromaticScaleBtnPanel.getComponents()).forEach(component -> {
            ((JButton) component).addActionListener((ActionEvent e) -> {
                inputNotes.append(((JButton) component).getText() + " ");
                invertedNotes.append(invertNote(((JButton) component).getText(), keyCenters) + " ");
            });
        });

        keyCenters.addChangeListener((ChangeEvent e) -> {
            String[] i = inputNotes.getText().split(" ");
            invertedNotes.setText("");

            for (String inputNote : i) {
                invertedNotes.append(invertNote(inputNote, keyCenters) + " ");
            }

            axisLabel.setIcon(null);

           /* Since the key center/chromatic scale list has an inverse order of indexes,
            * and because we know that the chromatic scale has 12 notes, we can
            * calculate an angle of the axis image in degrees for each key center with 
            * 11 - index * 360/12. Since C is the starting point and its index is 11,
            * no rotation will be applied in that case (11 - 11 * 30 = 0).
            */
           index = ((SpinnerListModel) keyCenters.getModel()).getIndex();
           degrees = (11.0 - index) * 30.0;
           drawAxis(axis, circleOfFifthsPanel, degrees%180);
        });

        JPanel inputNotesPanel = new JPanel(); 
        inputNotesPanel.add(chromaticScaleBtnPanel);
        
        // Create the rest of top-right portion of frame and modify constraints for each subcomponent
        JPanel controlPanel = new JPanel(new GridBagLayout()); 
        controlPanel.setPreferredSize(panelDimensions);
        controlPanel.setFont(font);
        controlPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        GridBagConstraints controlPanelConstraints = new GridBagConstraints();
        controlPanelConstraints.weightx = 1; controlPanelConstraints.weighty = 1; 

        controlPanelConstraints.gridx = 0; controlPanelConstraints.gridy = 0;
            controlPanel.add(keyCenters, controlPanelConstraints);

        controlPanelConstraints.gridx = 0; controlPanelConstraints.gridy = 1;
        controlPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
        controlPanelConstraints.gridwidth = 2;
            controlPanel.add(inputNotes, controlPanelConstraints);

        controlPanelConstraints.gridx = 0; controlPanelConstraints.gridy = 2;
            controlPanel.add(invertedNotes, controlPanelConstraints);

        JButton clearFields = new JButton("Clear");
        clearFields.addActionListener((ActionEvent e) -> {
            inputNotes.setText("");
            invertedNotes.setText("");
        });

        controlPanelConstraints.weightx = 0;
        controlPanelConstraints.gridx = 0; controlPanelConstraints.gridy = 3;
            controlPanel.add(clearFields, controlPanelConstraints);

        JPanel invertedNotesPanel = new JPanel();
        invertedNotesPanel.add(controlPanel);

        circleOfFifthsPanel.add(circleOfFifthsLabel);

        // Create main panel and modify constraints for each subcomponent
        JPanel mainPanel = new JPanel(new GridBagLayout()); 
        GridBagConstraints mainPanelConstraints = new GridBagConstraints();

        mainPanelConstraints.gridx = 0; mainPanelConstraints.gridy = 0; 
            mainPanel.add(inputNotesPanel, mainPanelConstraints);

        mainPanelConstraints.gridx = 1; mainPanelConstraints.gridy = 0; 
            mainPanel.add(invertedNotesPanel, mainPanelConstraints);

        mainPanelConstraints.gridx = 0; mainPanelConstraints.gridy = 1; 
        mainPanelConstraints.gridwidth = 2; mainPanelConstraints.gridheight = 2;
            mainPanel.add(circleOfFifthsPanel, mainPanelConstraints);

        // Create menubar
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem info = new JMenuItem("Info");
        
        info.setIcon(infoIcon);
        info.addActionListener((ActionEvent e) -> {
            JOptionPane.showMessageDialog(null,
                    infoText,
                    "Negative Harmony Inverter",
                    JOptionPane.INFORMATION_MESSAGE, infoIcon);
        });

        menu.add(info);
        menuBar.add(menu);

        setJMenuBar(menuBar);
        add(mainPanel);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        try {
            setIconImage(programIcon.getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        pack();
    }

    // Inverts a note over an axis.
    private String invertNote(String inputNote, JSpinner keyCenters) { 
        return !inputNote.isEmpty() ? 
            this.chromaticScale.getNote(inputNote,
                ((IntervalObject) this.chromaticScale.getScale().get(inputNote)).getInversionDistance() +
                ((IntervalObject) this.chromaticScale.getScale().get(((String) keyCenters.getValue()))).getKeyShift()
            )
        : "" ;
    }

    private void drawAxis(ImageIcon icon, JPanel panel, double deg) {
        Graphics g = panel.getGraphics();
        panel.paintComponents(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.rotate(deg / 180.0 * Math.PI, panel.getWidth()/2 + 10, panel.getHeight()/2);
        g2.drawImage(icon.getImage(), 0, 0, null);
    }
}