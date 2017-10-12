import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.swing.*;
import javax.imageio.*;
import java.io.*;
public class txt2Graph {
	//main function
	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				frame = new translatorFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}
	//show——frame
	static class translatorFrame extends JFrame{
		public translatorFrame(){
			setTitle("实验一：文本有向图");
			setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
			controlPanel control = new controlPanel();
			add(control,BorderLayout.SOUTH);
			label = new JLabel("");
			label.setLayout(new FlowLayout());
			add(label);
		}
		private static final int DEFAULT_WIDTH = 1000;
		private static final int DEFAULT_HEIGHT = 600;
	}
	//contro panel
	static class controlPanel extends JPanel{
		//all the buttons
		public controlPanel(){
			//setLayout(new BorderLayout());
			
			panel = new JPanel();
			//panel.setLayout(new GridLayout(3,2));
			
			//fun1:open file
			addButton("open file",new FileAction());
			
			//fun2:show
			addButton("show",new ShowAction());
			
			//fun3:bridge word
			addButton("bridge word",new WordBridgeAction());
			
			//fun4:new text
			addButton("new text",new NewTxtAction());
			
			//fun5:shortest path
			addButton("shortest path",new ShortedtPathAction());
			
			//fun6:random walk
			addButton("random walk",new RandomWalkAction());
			
			//addButton("clear",new ClearAction());
			add(panel,BorderLayout.CENTER);
		}
		private void addButton(String label,ActionListener listener){
			JButton button = new JButton(label);
			button.addActionListener(listener);
			panel.add(button);
		}
		private class FileAction implements ActionListener{
			public void readFileToWords(){
				File file = new File(fileName);
				BufferedReader reader = null;
				try{
					reader = new BufferedReader(new FileReader(file));
					String tempString = null;
					while((tempString = reader.readLine())!=null){
						String s = tempString.replaceAll("[\\p{Punct}]+", " ");  //标点变成空格
						//System.out.println(s);
						String[] Words = s.trim().split("\\s+");   //按空格分割
						
						for(int i = 0;i<Words.length;i++)   //正则表达式匹配字母并变成小写
						{
							Pattern p = Pattern.compile("a-z||A-Z");
							Matcher m = p.matcher(Words[i]);
							Words[i] = m.replaceAll("").trim().toLowerCase();
						}
						//System.out.println("line"+line+":"+tempString);
						for(String str :Words){
							write(fileName.substring(0, fileName.length()-4) +"Results.txt",str+"\r\n");
						}
					}
					reader.close();
				}catch(IOException e){
					e.printStackTrace();
				}finally{
					if(reader!=null){
						try{
							reader.close();
						}catch(IOException e1){
						}
					}
				}
			}
			public void readWordsToGraph(){
				graph = new DGraph();
				File file = new File(resultsFileName);
				BufferedReader reader = null;
				try{
					reader = new BufferedReader(new FileReader(file));
					String tempString = null;
					String word1 = null;
					String word2 = null;
					while((tempString = reader.readLine())!=null){
						word2 = tempString; 
						if(word1 != null){
							graph.addEdge(word1, word2);
						}
						word1 = tempString;
						//System.out.println(tempString);
					}
					reader.close();
				}catch(IOException e){
					e.printStackTrace();
				}finally{
					if(reader!=null){
						try{
							reader.close();
						}catch(IOException e1){
						}
					}
				}
			}
			public void actionPerformed(ActionEvent e){
				//file chooser
				JFileChooser jfc=new JFileChooser();  
		        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );  
		        jfc.showDialog(new JLabel(), "选择");  
		        File file=jfc.getSelectedFile();  
		        if(file==null){
		        	return;
		        }
		        if(file.isDirectory()){  
		            System.out.println("文件夹:"+file.getAbsolutePath());  
		        }else if(file.isFile()){  
		            System.out.println("文件:"+file.getAbsolutePath());  
		        } 
		        //System.out.println(jfc.getSelectedFile().getName());  
		        //read file
		        fileName = file.getAbsolutePath();
		        resultsFileName = fileName.substring(0, fileName.length()-4) +"Results.txt";
		        randomWalkFileName = resultsFileName.substring(0, resultsFileName.length()-4) +"RandomWalk"+ "." + "txt";
		        //System.out.println("pathGraphFileName: "+pathGraphFileName);
		        readFileToWords();
		        readWordsToGraph();
		        //graphFileName = graph.showGraph(resultsFileName);
			}
		}
		private class ShowAction implements ActionListener{
			public void actionPerformed(ActionEvent e){
				//graph.showGraph(resultsFileName);
				showDirectedGraph(graph);
			}
		}
		private class WordBridgeAction implements ActionListener{
			public void actionPerformed(ActionEvent e){

				String word1=null,word2=null;
				word1 = JOptionPane.showInputDialog("word1:");
				word2 = JOptionPane.showInputDialog("word2:");
				if(word1 ==null ||word2==null){
					JOptionPane.showMessageDialog(null, "please input two words!","bridge word", JOptionPane.INFORMATION_MESSAGE);
				}
				else{
					String wordBridge = graph.queryBridgeWords(word1, word2);
					JOptionPane.showMessageDialog(null, wordBridge,"bridge word", JOptionPane.INFORMATION_MESSAGE);
					System.out.println(word1+word2);
				}
			}
		}
		private class NewTxtAction implements ActionListener{
			public void actionPerformed(ActionEvent e){
				String sentence = null;
				sentence = JOptionPane.showInputDialog("sentence:");
				if(sentence!=null){
					String newText = graph.generateNewText(sentence);
					JOptionPane.showMessageDialog(null, newText,"new text", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
		private class ShortedtPathAction implements ActionListener{
			public void actionPerformed(ActionEvent e){
				String resultsShortestPath = null;
				String[]  resultsShortestPathes;
				String word1=null,word2=null;
				//JOptionPane.showInputDialog("word2:", JOptionPane.CANCEL_OPTION);
				word1 = JOptionPane.showInputDialog("word1:");
				word2 = JOptionPane.showInputDialog("word2:");
				System.out.println("word1: "+word1+" ,word2: "+word2);
				if(word2 == null){
					//必须取消输入word2才能进入
					resultsShortestPathes = graph.calcShortestPath(word1);
					for(String path:resultsShortestPathes){
						int res = JOptionPane.showConfirmDialog(null,"next are the shortest path begin with word "+word1,"continue or not",JOptionPane.YES_NO_OPTION);
						if(res == JOptionPane.NO_OPTION){
							break;
						}
						else{
							String srr[] = path.split("@");
							showPathes(graph,srr);
							String p[] = srr[0].split(" ");
							word2 = p[p.length-1];
							JOptionPane.showMessageDialog(null, "From \""+word1+"\" to \""+word2+"\" there are "+srr.length+" shortest path:   \r\n"+path,"shortest path", JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}else if(word1 != null &&word2!= null){
					resultsShortestPath = graph.calcShortestPath(word1, word2);
					String srr[] = resultsShortestPath.split("@");
					showPathes(graph,srr);
					JOptionPane.showMessageDialog(null, "From \""+word1+"\" to \""+word2+"\" there are "+srr.length+" shortest path:   \r\n"+resultsShortestPath,"shortest path", JOptionPane.INFORMATION_MESSAGE);
				}
				
			}
		}
		private class RandomWalkAction implements ActionListener{
			public void actionPerformed(ActionEvent e){
				String reslutsRandomWalk = graph.randomWalk();
				showPath(graph,reslutsRandomWalk);
				write(randomWalkFileName,reslutsRandomWalk+"\r\n");
				JOptionPane.showMessageDialog(null, "\""+reslutsRandomWalk+"\""+" has been stored in "+randomWalkFileName,"random walk",JOptionPane.INFORMATION_MESSAGE);
			}
		}
		private JPanel panel;
		private DGraph graph ;
		private static final int LENGTH_WORD = 10;
		private static final int LENGTH_SENT = 40;
	}
	//show directed graph
	static void showDirectedGraph(DGraph graph){
		 graphFileName = graph.showGraph(resultsFileName);
		 System.out.println(graphFileName);
		 img = new ImageIcon(graphFileName);
		 label.setIcon(img);
		 label.setText(null);
	 }
	static void showPath(DGraph graph,String Path){
		 pathGraphFileName = graph.showGraphPath(resultsFileName, Path);
		 System.out.println("pathGraphFileName:"+pathGraphFileName);
		 img = new ImageIcon(pathGraphFileName);
		 label.setIcon(img);
		 label.setText(null);
	 }
	static void showPathes(DGraph graph,String[] Pathes){
		pathGraphFileName = graph.showGraphPathes(resultsFileName, Pathes);
		System.out.println("pathGraphFileName:"+pathGraphFileName);
		img = new ImageIcon(pathGraphFileName);
		label.setIcon(img);
		label.setText(null);
	}
	static void write(String fileName,String str){
		try{
			//new File(fileName);
			FileWriter writer = new FileWriter(fileName,true);
			writer.write(str);;
			//System.out.println("写入文件成功");
			writer.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	 private static JLabel label;
	 private static String fileName = null;
	 private static String resultsFileName = null;
	 private static String graphFileName = null;
	 private static String pathGraphFileName = null;
	 private static String randomWalkFileName = null;
	 private static ImageIcon img;
	 //private static String graphFileName = "C://Users//ibm//Desktop//testResults.gif";
	 private static translatorFrame frame ;
}
