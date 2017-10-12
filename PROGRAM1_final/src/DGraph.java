import java.util.regex.Matcher;
import java.util.Random;
import java.util.regex.*;
import java.awt.Color;
import java.io.File;
public class DGraph {
	public DGraph(){
		vertex = 0;
		edge = 0;
	}
	public void addEdge(String word1,String word2){
		int index1 = -1;
		int index2 = -1;
		//找到单词1对应的节点，或者创建一个
		for(int i = 0;i<vertex;i++){
			if(adj[i].getHead().word.equals(word1)){
				index1 = i;
			}
		}
		if(-1 == index1){
			adj[vertex] = new LinkedList(word1);
			index1 = vertex;
			vertex++;
		}
		//找到单词2对应的节点，或者创建一个
		for(int i = 0;i<vertex;i++){
			if(adj[i].getHead().word.equals(word2)){
				index2 = i;
			}
		}
		if(-1 == index2){
			adj[vertex] = new LinkedList(word2);
			index2 = vertex;
			vertex++;
		}
		for(Node node =adj[index1].getHead().next;node!=null;node=node.next){
			if(node.word.equals(word2)){
				node.weight++;
				return;
			}
		}
		adj[index1].addNode(word2,index2);
	}
	public String queryBridgeWords(String word1,String word2){
		int index1 = -1,index2=-1,index3;
		Node node3,node4;
		for(int i=0;i<vertex;i++){
			if(word1.equals(adj[i].getHead().word)){
				index1 = i;
			}
			if(word1.equals(adj[i].getHead().word)){
				index2 = i;
			}
		}
		if(index1==-1||index2==-1){
			return "No \""+word1+"\" or \""+word2+"\" in the graph!";
		}
		String words = "";
		node3 = adj[index1].getHead().next;
		while(node3!= null){
			index3 = node3.num;
			node4 = adj[index3].getHead().next;
			while(node4!=null){
				if(node4.word.equals(word2)){
					words = words+node3.word+",";
					break;
				}
				node4 = node4.next;
			}
			node3 = node3.next;
		}
		if(words.equals("")){
			return "No bridge words from \""+word1+"\" to \""+word2+"\"!";
		}else{
			return "The bridge words from \""+word1 +"\" to \""+word2 +"\" are:"+words;
		}
		
	}
	public String oneBridgeWord(String word1,String word2){
		int index1 = -1,index2=-1,index3;
		Node node3,node4;
		for(int i=0;i<vertex;i++){
			if(word1.equals(adj[i].getHead().word)){
				index1 = i;
			}
			if(word1.equals(adj[i].getHead().word)){
				index2 = i;
			}
		}
		if(index1==-1||index2==-1){
			return null;
		}
		node3 = adj[index1].getHead().next;
		while(node3!= null){
			index3 = node3.num;
			node4 = adj[index3].getHead().next;
			while(node4!=null){
				if(node4.word.equals(word2)){
					return node3.word;
				}
				node4 = node4.next;
			}
			node3 = node3.next;
		}
		return null;
	}
	public String generateNewText(String inputText){
		String s = inputText.replaceAll("[\\p{Punct}\\p{Space}]+", " ");  //标点变成空格
		String[] words = s.trim().split("\\s+");   //按空格分割
		if(words.length<=2){
			return "the input text should be longer";
		}
		String text = words[0];
		String word1,word2,word3;
		for(int index=0;index<words.length-1;index++){
			word1= words[index].toLowerCase();
			word2= words[index+1].toLowerCase();
			word3=oneBridgeWord(word1,word2);
			if(word3 == null){
				text = text +" "+word2;
			}else{
				text = text +" "+word3 +" "+word2;
			}
		}
		return text;
	}
	public String randomWalk()  //随机游走,随机选择节点开始游走
	{
		int VertexNum = vertex;  //节点个数
		String Reply = new String();
		int MAXEdgeNum = 0;    //顶点最大出度
		for(int i = 0 ;i< VertexNum;i++)
		{
			if(adj[i].nodeNum > MAXEdgeNum)
				MAXEdgeNum = adj[i].nodeNum;
		}
		Random r = new Random();
		int [][] WalkVisited = new int[VertexNum][MAXEdgeNum];  //标记走过的边
		for(int i = 0;i<VertexNum;i++)
		{
			for(int j = 0 ;j < MAXEdgeNum;j++)
				WalkVisited[i][j] = -1;
		}
		int WalkVertex = -1;   //随机游走起始的节点索引
		while(WalkVertex < 0)
			WalkVertex = r.nextInt() % VertexNum; 
		StringBuilder WordBuilder = new StringBuilder();
		String firstword = adj[WalkVertex].getHead().word;
		WordBuilder.append(firstword);
		while(true)
		{
			int next = -1;  //随机的边
			if(adj[WalkVertex].nodeNum == 0)
				break;    //顶点的出度为0
			while(next < 0)
				next = r.nextInt() % adj[WalkVertex].nodeNum;
			Node nextNode = adj[WalkVertex].getHead().next;
			for(int j = 0 ;j < next;j++)
				nextNode = nextNode.next;
			
			int nextVertex = nextNode.num;        //游走的下一条边
			WordBuilder.append(" "+adj[nextVertex].getHead().word);
			boolean flag = false;
			int EdgeNum = 0;
			for(int j = 0; j < adj[WalkVertex].nodeNum;j++)  //查找边是否已走过
			{
				if (WalkVisited[WalkVertex][j] == -1)
				{
					EdgeNum = j;
					break;
				}
				else if (WalkVisited[WalkVertex][j] == nextVertex)
				{
					flag = true;
					break;
				}
				
			}
			
			if(flag)    //如果访问过推出
				break;
			else        //否则继续寻边
			{
				WalkVisited[WalkVertex][EdgeNum] = nextVertex;
				WalkVertex = nextVertex;
			}
				
		}
		Reply = WordBuilder.toString();
		return Reply;
	}

	public String showGraph(String resultsFileName)
	{//return fileName
		GraphViz gv = new GraphViz();
		 gv.addln(gv.start_graph());
		 for(int i=0;i<vertex;i++){
			 Node head = adj[i].getHead();
			 Node tail = adj[i].getHead().next;
			 while(null != tail){
				 gv.addln(head.word + "->" + tail.word+"[label="+tail.weight+"]");
				 tail = tail.next;
			 }
		 }
		 gv.addln(gv.end_graph());
		 System.out.println(gv.getDotSource());


        String type = "gif";
        // String type = "dot";
        // String type = "fig"; // open with xfig
        // String type = "pdf";
        // String type = "ps";
        // String type = "svg"; // open with inkscape
        // String type = "png";
        // String type = "plain";
        String graphFileName = resultsFileName.substring(0, resultsFileName.length()-4) + "." + type;
        File out = new File(graphFileName);
        gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
		return graphFileName;
	}
	public String showGraphPath(String resultsFileName,String Path){
		String srr[] = Path.split(" ");
		GraphViz gv = new GraphViz();
		 gv.addln(gv.start_graph());
		 for(int i=0;i<vertex;i++){
			 Node head = adj[i].getHead();
			 Node tail = adj[i].getHead().next;
			 while(null != tail){
				 gv.addln(head.word + "->" + tail.word+"[label="+tail.weight+"]");
				 tail = tail.next;
			 }
		 }
		 String temp1 = null,temp2 = null;
		 for(String s:srr){
			 temp1 = temp2;
			 temp2 = s;
			 if(temp1!=null){
				 gv.addln(temp1+"->"+temp2+"[color=red]");
			 }
		 }
		 gv.addln(gv.end_graph());
		 System.out.println(gv.getDotSource());


       String type = "gif";
       // String type = "dot";
       // String type = "fig"; // open with xfig
       // String type = "pdf";
       // String type = "ps";
       // String type = "svg"; // open with inkscape
       // String type = "png";
       // String type = "plain";
       String pathGraphFileName = resultsFileName.substring(0, resultsFileName.length()-4) +"Path"+version+ "." + type;
       File out = new File(pathGraphFileName);
       gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
       version++;
		return pathGraphFileName;
	}
	public String showGraphPathes(String resultsFileName,String[] Pathes){
		
		GraphViz gv = new GraphViz();
		 gv.addln(gv.start_graph());
		 for(int i=0;i<vertex;i++){
			 Node head = adj[i].getHead();
			 Node tail = adj[i].getHead().next;
			 while(null != tail){
				 gv.addln(head.word + "->" + tail.word+"[label="+tail.weight+"]");
				 tail = tail.next;
			 }
		 }
		 //private RandomGenerator rgen = new RandomGenerator();
		 String[] color = {"red","orange","blue","yellow","green","pink","purple"};
		 Random rand = new Random();
		 for(String path :Pathes){
			// Color color = 
			 String srr[] = path.split(" ");
			 String temp1 = null,temp2 = null;
			 String c = color[rand.nextInt(color.length)];
			 for(String s:srr){
				 temp1 = temp2;
				 temp2 = s;
				 if(temp1!=null){
					 gv.addln(temp1+"->"+temp2+"[color="+c+"]");
				 }
				 
			 }
		 }
		 gv.addln(gv.end_graph());
		 System.out.println(gv.getDotSource());


       String type = "gif";
       // String type = "dot";
       // String type = "fig"; // open with xfig
       // String type = "pdf";
       // String type = "ps";
       // String type = "svg"; // open with inkscape
       // String type = "png";
       // String type = "plain";
       String pathGraphFileName = resultsFileName.substring(0, resultsFileName.length()-4) +"Path"+version+ "." + type;
       File out = new File(pathGraphFileName);
       gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
       version++;
		return pathGraphFileName;
	}
	//最短路径，Dijkstra算法改进，求多条最短路径
	public String calcShortestPath(String word1,String word2)  
		{
			int[][] path = new int[vertex][vertex];  //记录一点最短路径的上一节点
			boolean[] visited = new boolean[vertex];   //标记是否访问过
			int[] dist = new int[vertex];
			int index1 = -1;      //word1的索引
			int index2 = -1;      //word2的索引
			String Reply = new String();
			boolean flag = false;   //判断word2是否可达
			for(int i = 0;i< vertex;i++)
			{
				if(adj[i].getHead().word.equals(word1))  //找到以word1为顶点的边
					index1 = i;
				if(adj[i].getHead().word.equals(word2))  //找到以word2为顶点的边
					index2 = i;
				dist[i] = MAX;
				visited[i] = false;
				for(int j = 0;j< vertex;j++)
					path[i][j] = -1;
			}
			
			if(index1 == -1 || index2 == -1)
				return "单词不存在！";
			visited[index1] = true;
			dist[index1] = 0;
			Node Vertex = adj[index1].getHead().next;    //word1后链接的点
			while(Vertex != null)
			{
				dist[Vertex.num] = Vertex.weight;
				path[Vertex.num][0] = index1;   //可能会有几条路径
				Vertex = Vertex.next;
			}
			for(int i = 1;i< vertex;i++)          //z最多循环(V-1)次
			{
				int MINdist = MAX;           //当前的最短路径
				int interVertex = index1;    //途经的中间点
				for(int j = 0;j<vertex;j++)
				{
					if(!visited[j] && dist[j] < MINdist)
					{
						MINdist = dist[j];
						interVertex = j;
					}
				}
				int[] interVertexs = new int[vertex];
				int interVertexNum = 0;
				for(int j = 0;j<vertex;j++)
				{
					if(!visited[j] && dist[j] == MINdist)
					{
						interVertexs[interVertexNum] = j;
						visited[j] = true;
						interVertexNum ++;
					}
				}
				
				if(visited[index2])   //判断是否是word2
				{
					flag = true;
					break;
				}
				
				
				for(int k = 0; k < vertex;k++)
				{
					if(visited[k])     //对于未访问过的节点，判断最短路径经过哪些已访问过的点
					{
						Node NotVisited = adj[k].getHead().next;
						while(NotVisited != null )
						{
							if(!visited[NotVisited.num])    //未访问过的点可以更新dist
							{
								if(dist[NotVisited.num] > dist[k] + NotVisited.weight)  //需要更新
								{
									dist[NotVisited.num] = dist[k] + NotVisited.weight;
									//改变path
									path[NotVisited.num][0] = k;
									for(int z = 1;z<vertex;z++)
									{
										if(path[NotVisited.num][z] >= 0)
										    path[NotVisited.num][z] = -1;
										else
											break;
									}
								}
								else if(dist[NotVisited.num] == dist[k] + NotVisited.weight)  //需要加入新的path
								{
									for(int z = 0;z<vertex;z++)
									{
										if(path[NotVisited.num][z] ==-1)
										{
											path[NotVisited.num][z] = k;
											break;
										}
									}
								}
							}
							NotVisited = NotVisited.next;
						}
					}
				}
			}
			if(flag == false)
			{
				Reply = "不可达！";
				return Reply;
			}
			Reply = DisplayPath(index1,index2,path);
			String[] WordSplit = Reply.split("@");
			StringBuilder ReplyBuilder = new StringBuilder();
			
			for(int j = 0 ; j < WordSplit.length ; j ++)
			{
				WordSplit[j] = WordSplit[j] + " " + word2;
				ReplyBuilder.append(WordSplit[j]+"@");
			}
			return ReplyBuilder.toString();
		}
		
	public String DisplayPath(int start,int end,int[][] path)
		{//求多条最短路径时输出路径  中间@间隔，采用递归，倒叙
		 //start为起始的源点，end为终点，path为二维数组
			if (start == end)
				return adj[start].getHead().word;
			StringBuilder builder = new StringBuilder();
			for(int i = 0 ; i < vertex ; i ++)
			{
				if(path[end][i] != -1)
				{
					StringBuilder Wordbuilder = new StringBuilder();
					String MidString = DisplayPath(start,path[end][i],path);
					String[] PathWords = MidString.split("@"); //按@分开，存储的时候每条路径以@分割
					for(int j = 0 ;j < PathWords.length;j++)
					{
						if(path[end][i] != start)
							PathWords[j] = PathWords[j] +" " +  adj[path[end][i]].getHead().word;
						
						Wordbuilder.append(PathWords[j] + "@");
					}
					builder.append(Wordbuilder.toString());
				}
			}
			return builder.toString();
		}
		
	public String[] calcShortestPath(String word1)  //最短路径，Dijkstra算法,只有一个参数
		{
			int[] path = new int[vertex];  //记录经过的点
			boolean[] visited = new boolean[vertex];   //标记是否访问过
			int[] dist = new int[vertex];
			int index1 = -1;      //word1的索引
			//int index2 = -1;      //word2的索引
			String[] Reply = new String[vertex-1];
			for(int i = 0;i< vertex;i++)
			{
				if(adj[i].getHead().word.equals(word1))  //找到以word1为顶点的边
					index1 = i;
				dist[i] = MAX;
				visited[i] = false;
				
			}
			if(index1 == -1)
			{
				Reply[0] =  "单词不存在！";
				return Reply;
			}
				
			visited[index1] = true;
			dist[index1] = 0;
			Node Vertex = adj[index1].getHead().next;    //word1后链接的点
			while(Vertex != null)
			{
				dist[Vertex.num] = Vertex.weight;
				path[Vertex.num] = index1;
				Vertex = Vertex.next;
			}
			for(int i = 1;i< vertex;i++)          //z最多循环(V-1)次
			{
				int MINdist = MAX;           //当前的最短路径
				int interVertex = index1;    //途经的中间点
				for(int j = 0;j<vertex;j++)
				{
					if(!visited[j] && dist[j] < MINdist)
					{
						MINdist = dist[j];
						interVertex = j;
					}
				}
				visited[interVertex] = true;
					
				Node interNode = adj[interVertex].getHead().next;
				while(interNode != null)    //更新dist
				{
					if(!visited[interNode.num] && dist[interNode.num]>dist[interVertex]+interNode.weight)
					{
						dist[interNode.num] = dist[interVertex]+interNode.weight;
						path[interNode.num] = interVertex;
					}
					interNode = interNode.next;
				}
			}
			String[] WordPath = new String[vertex];
			int num = 0;
			for(int i = 0;i<vertex;i++)
			{
				if(visited[i] == false)
				{
					Reply[num] = adj[i].getHead().word+" 不可达！";
					num++;
				}
					
			}
			for(int i = 0; i< vertex;i++)   //连向各个点的最短路径
			{
				if(i != index1 && visited[i] == true)
				{
					WordPath[0] = adj[i].getHead().word;
					int PathIndex = i;
					int PathNum = 1;
					while(path[PathIndex] != index1)      //将途径的单词加入到WordPath中，为倒序
					{
						PathIndex = path[PathIndex];
						WordPath[PathNum] = adj[PathIndex].getHead().word;
						PathNum++;
					}
					StringBuilder builder = new StringBuilder();
					builder.append(word1);
					for(int k = PathNum-1;k>=0;k--)
					{
						builder.append(" "+WordPath[k]);
					}
					Reply[num] = builder.toString();
					num++;
				}
			}
			
			return Reply;
		}
	private int vertex;//num of vertex
	private int edge;//num of edge
	private LinkedList[] adj = new LinkedList[MAXNode];
	private int version = 1;
	
	public static final int MAXNode = 100;
	public static final int MAX = 32767;
	private static final int infinite = 1000;
}

class Node{
	public String word;
	public int weight;
	public Node next;
	public int num;
	public boolean painted;
	public Node(){
		word = null;
		next = null;
		num = weight = 0;
	}
	public Node(String w,int n){
		word = w;
		weight = 1;
		next = null;
		num = n;
	}
}

class LinkedList{
	private Node head = null;
	private Node tail = null;
	public int nodeNum;
	public LinkedList(){
		nodeNum = -1;
	}
	public LinkedList(String w){
		nodeNum = 0;
		Node newNode = new Node(w,-1);
		head = newNode;
		tail = newNode;
	}
	public boolean isEmpty(){

		if(head==null)
			return true;
		else
			return false;
	}
	public void addNode(String w,int n){
		Node newNode = new Node(w,n);
		if(isEmpty()){
			head = tail = newNode;
		}else{
			tail.next = newNode;
			tail = newNode;
		}
		nodeNum++;
	}
	public Node getHead(){
		return head;
	}
	public Node getTail(){
		return tail;
	}
}