import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RectangularShape;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import automaton.State;
import automaton.StateImpl;
import automaton.Transition;
import automaton.TransitionImpl;
import machine.TransitionWithAction;

public class GraphFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private GraphComponent component;
	private FramesController controller;

	public GraphFrame(FramesController controller) {
		this.controller = controller;

		component = new GraphComponent();
		component.setForeground(Color.BLACK);
		component.setBackground(Color.WHITE);
		component.setOpaque(true);
		component.setPreferredSize(new Dimension(1000, 1000));
		JScrollPane scrollPane = new JScrollPane(component);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu menu = new JMenu(GraphEditor.MENU_FILE);
		menuBar.add(menu);
		createMenuItem(menu, GraphEditor.MENU_ITEM_NEW, new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				GraphFrame.this.controller.createFrame();
			}
		});
		createMenuItem(menu, GraphEditor.MENU_ITEM_CLOSE, new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				GraphFrame.this.controller.deleteFrame(GraphFrame.this);
			}
		});
		createMenuSeparator(menu);
		createMenuItem(menu, GraphEditor.MENU_ITEM_QUIT, new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				GraphFrame.this.controller.quit();
			}
		});

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				GraphFrame.this.controller.deleteFrame(GraphFrame.this);
			}
		});

		// toolbar
		JToolBar toolbar = new JToolBar();
		toolbar.setLayout(new GridLayout(0, 1));
		JButton b = addShapeButton(toolbar, new Ellipse2D.Double(0, 0, 50, 50), "Initial state");
		b.doClick();
		addShapeButton(toolbar, new Ellipse2D.Double(0, 0, 50, 50), "State");
		addShapeButton(toolbar, new Ellipse2D.Double(0, 0, 60, 60), "Final state");
		addShapeButton(toolbar, new Ellipse2D.Double(0, 0, 60, 60), "Initial Final state");

		JToolBar toolbar2 = new JToolBar();
		toolbar2.setLayout(new GridLayout(2, 2));
		// button Update automaton
		JButton b21 = new JButton("Update automaton");
		b21.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				component.updateLabel();
				component.updateDFA();
			}
		});
		toolbar2.add(b21);
		// button Test
		JButton b22 = new JButton("Test");
		b22.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (component.machine != null) {
					String test;
					test = JOptionPane.showInputDialog("Please input string to check recognized by automaton: ");
					if (component.machine.recognize(test.split("(?!^)")) == true) {
						JOptionPane.showMessageDialog(null,
								"Read: " + component.wordcross + ";\n" + test + " is recognized by automaton",
								"Result: True", JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null,
								"Read: " + component.wordcross + ";\n" + test + " is not recognized by automaton",
								"Result: False", JOptionPane.INFORMATION_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(null, "No automaton created yet. Create it first!", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
				component.wordcross = "";
			}
		});
		toolbar2.add(b22);
		// button Save automaton
		JButton b23 = new JButton("Save into file");
		b23.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();
					Document doc = builder.newDocument();

					// root element
					Element rootElement = doc.createElement("root");
					doc.appendChild(rootElement);

					// states list
					for (int i = 0; i < component.stateList.size(); i++) {
						// states element
						Element states = doc.createElement("states");
						rootElement.appendChild(states);
						// setting attribute to element stateList
						states.setAttribute("id", i + "");
						// setting subelement of statelist
						Element initial = doc.createElement("initial");
						initial.appendChild(doc.createTextNode(component.stateList.get(i).initial() + ""));
						states.appendChild(initial);
						Element terminal = doc.createElement("terminal");
						terminal.appendChild(doc.createTextNode(component.stateList.get(i).terminal() + ""));
						states.appendChild(terminal);
					}

					// transitions list
					for (int i = 0; i < component.transitionList.size(); i++) {
						// states element
						Element transitions = doc.createElement("transitions");
						rootElement.appendChild(transitions);
						// setting attribute to element transitions List
						transitions.setAttribute("id", i + "");
						// setting subelement of transitions list
						Element source = doc.createElement("source");
						source.appendChild(doc.createTextNode(
								component.stateList.indexOf(component.transitionList.get(i).source()) + ""));
						transitions.appendChild(source);
						Element target = doc.createElement("target");
						target.appendChild(doc.createTextNode(
								component.stateList.indexOf(component.transitionList.get(i).target()) + ""));
						transitions.appendChild(target);
						Element label = doc.createElement("label");
						label.appendChild(doc.createTextNode(component.transitionList.get(i).label()));
						transitions.appendChild(label);
					}

					// write the content into xml file
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					Transformer transformer = transformerFactory.newTransformer();
					DOMSource sourcedoc = new DOMSource(doc);
					// choose directory to save file
					String myPath = null;
					JFileChooser chooser = new JFileChooser();
					FileNameExtensionFilter filter = new FileNameExtensionFilter("XML files (*.xml)", "xml");
					chooser.setFileFilter(filter);
					chooser.setDialogTitle("Select directory to save file");
					if (chooser.showSaveDialog(component) == JFileChooser.APPROVE_OPTION) {
						myPath = chooser.getSelectedFile().getAbsolutePath();
						if (!myPath.toLowerCase().endsWith(".xml")) {
							myPath += ".xml";
						}
					}
					StreamResult result = new StreamResult(new File(myPath));
					transformer.transform(sourcedoc, result);
					// Output to console for testing
					StreamResult consoleResult = new StreamResult(System.out);
					transformer.transform(sourcedoc, consoleResult);

				} catch (ParserConfigurationException | TransformerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		toolbar2.add(b23);
		// button Upload from file
		JButton b24 = new JButton("Upload from file");
		b24.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				if (component.stateList.size() != 0) {
					JOptionPane.showMessageDialog(null, "Remove all existing data before uploading automaton.",
							"Cannot upload automaton!", JOptionPane.ERROR_MESSAGE);
				} else {
					try {
						// choose directory
						String myPath = null;
						JFileChooser chooser = new JFileChooser();
						FileNameExtensionFilter filter = new FileNameExtensionFilter("XML files (*.xml)", "xml");
						chooser.setFileFilter(filter);
						chooser.setDialogTitle("Select file to upload");
						if (chooser.showDialog(component, "Upload") == JFileChooser.APPROVE_OPTION) {
							myPath = chooser.getSelectedFile().getAbsolutePath();
						}
						File inputFile = new File(myPath);
						DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
						DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
						Document doc = docBuilder.parse(inputFile);

						// Read doc
						Node rootNode = doc.getFirstChild();

						// states nodes
						NodeList states = doc.getElementsByTagName("states");
						for (int i = 0; i < states.getLength(); i++) {
							Node state = states.item(i);
							component.stateList.add(new StateImpl(false, false));
							// Children
							NodeList subel = state.getChildNodes();
							component.stateList.get(i)
									.setInitial(Boolean.parseBoolean(subel.item(0).getFirstChild().getNodeValue()));
							component.stateList.get(i)
									.setTerminal(Boolean.parseBoolean(subel.item(1).getFirstChild().getNodeValue()));
						}

						// transitions nodes
						NodeList transitions = doc.getElementsByTagName("transitions");
						for (int i = 0; i < transitions.getLength(); i++) {
							Node transition = transitions.item(i);
							component.transitionList.add(
									new TransitionWithAction(new TransitionImpl(null, null, null), component.print));
							// Children
							NodeList subel = transition.getChildNodes();
							component.transitionList.get(i).setSource(component.stateList
									.get(Integer.parseInt(subel.item(0).getFirstChild().getNodeValue())));
							component.transitionList.get(i).setTarget(component.stateList
									.get(Integer.parseInt(subel.item(1).getFirstChild().getNodeValue())));
							component.transitionList.get(i).setLabel(subel.item(2).getFirstChild().getNodeValue());
						}

						// draw automaton
						for (int i = 0; i < component.stateList.size(); i++) {
							boolean init = component.stateList.get(i).initial();
							boolean term = component.stateList.get(i).terminal();
							RectangularShape sample;
							if (term == false) {
								sample = new Ellipse2D.Double(0, 0, 50, 50);
							} else {
								sample = new Ellipse2D.Double(0, 0, 60, 60);
							}
							sample.setFrameFromCenter(100 * (i + 1), 150, 100 * (i + 1) + sample.getWidth() / 2,
									150 + sample.getHeight() / 2);
							component.vertices
									.add(new Vertex(sample, 's' + Integer.toString(i), init, term, Color.BLACK));
						}
						Vertex start, end;
						String lbl;
						for (int i = 0; i < component.transitionList.size(); i++) {
							start = component.vertices
									.get(component.stateList.indexOf(component.transitionList.get(i).source()));
							end = component.vertices
									.get(component.stateList.indexOf(component.transitionList.get(i).target()));
							lbl = component.transitionList.get(i).label();
							component.edges.add(new Edge(start, end));
							component.edges.get(i).jointPoints
									.add(new Ellipse2D.Double((start.shape.getCenterX() + end.shape.getCenterX()) / 2,
											150 + 50 * (i + 1), 10, 10));
							if (start == end) {
								component.edges.get(i).jointPoints.add(new Ellipse2D.Double(
										start.shape.getCenterX() + 50, 150 + 50 * (i + 1), 10, 10));
							}
							component.addEdgeLabel(component.edges.get(i), lbl);
						}
						component.repaint();
						component.updateLabel();
						component.updateDFA();
					} catch (ParserConfigurationException | SAXException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		toolbar2.add(b24);
		// Position of toolbars and scrollPane
		Container contentPane = getContentPane();
		contentPane.add(toolbar, BorderLayout.WEST);
		contentPane.add(toolbar2, BorderLayout.NORTH);
		contentPane.add(scrollPane, BorderLayout.CENTER);

		// Information message while getting start
		JOptionPane.showMessageDialog(null,
				"<html>* Click left buttons to create states."
						+ "<br>* Press 'Alt', press and drag mouse to create transitions between states.</br>"
						+ "<br>* While creating a transition, if you want to create jointpoint then push spacebar.</br>"
						+ "<br>* Click 'Update automaton' button to create or update the automaton.</br>"
						+ "<br>* Right-click if you want to delete state/transition/jointpoint.</br>"
						+ "<br>* Click 'Test' button to check if the automaton recognize input string (e.g. abccbade).</br>"
						+ "<br>* Use 'Save into file', 'Upload from file' buttons to save and reuse your built automaton.</br><html>",
				"Quick instruction", JOptionPane.INFORMATION_MESSAGE);
	}

	private JButton addShapeButton(JToolBar toolbar, final RectangularShape sample, String name) {
		JButton button = new JButton(name);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				component.setShapeType(sample);
				switch (name) {
				case "Initial state":
					component.setInitial(true);
					component.setFinal(false);
					break;
				case "Final state":
					component.setInitial(false);
					component.setFinal(true);
					break;
				case "Initial Final state":
					component.setInitial(true);
					component.setFinal(true);
					break;
				default:
					component.setInitial(false);
					component.setFinal(false);
				}
			}
		});
		toolbar.add(button);
		return button;
	}

	private void createMenuItem(JMenu menu, String name, ActionListener action) {
		JMenuItem menuItem = new JMenuItem(name);
		menuItem.addActionListener(action);
		menu.add(menuItem);
	}

	private void createMenuSeparator(JMenu menu) {
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.lightGray);
		menu.add(separator);
	}
}
