/***
References:
http://www.sanfoundry.com/java-program-implement-ford-fulkerson-algorithm/
http://algs4.cs.princeton.edu/64maxflow/FordFulkerson.java.html
http://www.geeksforgeeks.org/ford-fulkerson-algorithm-for-maximum-flow-problem/
http://www.geeksforgeeks.org/breadth-first-traversal-for-a-graph/
***/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.StringTokenizer;


public class TaskAllocator 
{
	static int numberOfNodes, numberOfTasks;
	static int[] parent;
	public static void main(String args[]) throws IOException
	{
		Scanner scanner = new Scanner(new InputStreamReader(System.in));
        
        System.out.println("Enter path for the input file:");
        String taskFilePath = scanner.nextLine();
        taskFilePath = taskFilePath.replace("/", File.separator);
        
        System.out.println("Enter file path for output file:");
        String outputFilePath = scanner.nextLine();
        outputFilePath = outputFilePath.replace("/", File.separator);
		scanner.close();

		TaskAllocator ta = new TaskAllocator();
		int graph[][] = ta.getGraph(taskFilePath);
		
		numberOfNodes = graph.length -1;
		parent = new int[numberOfNodes + 1];
		
		int[][] residualGraph = ta.getAllocation(graph, 1, numberOfTasks + 2);
		
		ta.generateOutput(residualGraph, graph, outputFilePath);
	}
	
	public void generateOutput(int[][] residualGraph, int[][] originalGraph, String outputFilePath) throws IOException
	{		
		File output = new File(outputFilePath);
		try
		{
			if(output.exists() == false)
			{
	            output.createNewFile();
			}
		}	
		catch(IOException e)
		{
	        System.out.println("Error in writing output");
		}
		
		for(int i = 2; i <= numberOfTasks + 1; i++)
		{
			for(int j = numberOfTasks + 3; j < residualGraph.length; j++)
			{
				if(originalGraph[i][j] == 1 && residualGraph[i][j] == 0)
				{												
					PrintWriter writer = new PrintWriter(new FileWriter(output, true));
					writer.append("TaskID " + (i-1) + " : Worker ID " + (j - numberOfTasks - 2) + System.lineSeparator());
					writer.close();
				}
			}
		}
	}
	
	public int[][] getAllocation(int[][] graph, int source, int sink)
	{
		int u, v, pathFlow = 0;
		int[][] residualGraph = new int[numberOfNodes + 1][numberOfNodes + 1];
		
        for (int sourceNode = 1; sourceNode <= numberOfNodes; sourceNode++)
        {
            for (int destinationNode = 1; destinationNode <= numberOfNodes; destinationNode++)
            {
                residualGraph[sourceNode][destinationNode] = graph[sourceNode][destinationNode];
            }
        }
		
        while(hasPath(residualGraph, source, sink))
        {
        	pathFlow = Integer.MAX_VALUE;
        	for (v = sink; v != source; v = parent[v])
            {
                u = parent[v];
                pathFlow = Math.min(pathFlow, residualGraph[u][v]);
            }

            for (v = sink; v != source; v = parent[v])
            {
                u = parent[v];
                residualGraph[u][v] -= pathFlow;
                residualGraph[v][u] += pathFlow;
            }         	
        }
		return residualGraph;
	}
	
	//BFS to find if path exists from given source to sink
	public boolean hasPath(int[][] graph, int source, int sink)
	{
		boolean pathFound = false;
		boolean[] visited = new boolean[numberOfNodes + 1];
		Queue<Integer> queue = new LinkedList<Integer>();
		int currentElement, destination;
		
		for(int i = 1; i <= numberOfNodes; i++)
		{
			parent[i] = -1;
		}
		
		queue.add(source);
		visited[source] = true;
		parent[source] = -1;
		
		while(!queue.isEmpty())
		{
			currentElement = queue.remove();
			destination = 1;
			
			while(destination <= numberOfNodes)
			{
				if(visited[destination] == false && graph[currentElement][destination] > 0)
				{
                    parent[destination] = currentElement;
                    queue.add(destination);
                    visited[destination] = true;
				}
				destination++;
			}
		}
		if(visited[sink] == true)
		{
			pathFound = true;
		}
		
		return pathFound;		
	}
	
	public int[][] getGraph(String taskFilePath) throws IOException
	{
		int i,j;
		
		BufferedReader br = new BufferedReader(new FileReader(taskFilePath));		
		String firstLine = br.readLine();
		String[] splittedFirstLine = firstLine.split(" ");
		int numberOfWorkers = Integer.parseInt(splittedFirstLine[1]);
		br.close();

		int[][] taskDetails = transferFileToMatrix(taskFilePath, numberOfWorkers);
		
		numberOfTasks = taskDetails.length;				
		int totalNumberOfNodes = numberOfWorkers + numberOfTasks;
		
		int[][] graph = new int[totalNumberOfNodes + 3][totalNumberOfNodes + 3];
				
		//connecting src node to all task nodes
		for(i = 2; i <= numberOfTasks + 1; i++)
		{
			graph[1][i] = 1;
		}
		
		//connecting all worker nodes to sink node
		for(i = numberOfTasks + 3; i < graph.length; i++)
		{
			graph[i][numberOfTasks + 2] = 1;
		}
		
		//using taskDetails to connect task and worker nodes
		for(i = 0; i < numberOfTasks; i++)
		{
			for(j = 1; j < numberOfWorkers + 1; j++)
			{
				if(taskDetails[i][j] != 0)
				{
					int columnNumber = taskDetails[i][j] + numberOfTasks + 2;
					graph[i+2][columnNumber] = 1;
				}
			}
		}
		return graph;
	}
	
	public int[][] transferFileToMatrix(String filePath, int numberOfWorkers) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String fileContents;
		try 
	    {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) 
	        {
	            sb.append(line);
	            sb.append(System.lineSeparator());
	            line = br.readLine();
	        }
	        fileContents = sb.toString();
	    } 
	    finally 
	    {
	        br.close();
	    }
			
		String[] inputLines = fileContents.split(System.lineSeparator());
		String splittedFirstLine[] = inputLines[0].split(" ");
		int numberOfRows = Integer.parseInt(splittedFirstLine[0]);
        
		int[][] matrixToFill = new int[numberOfRows][numberOfWorkers + 3];
		
		for(int i = 0; i< numberOfRows; i++)
		{
			StringTokenizer st = new StringTokenizer(inputLines[i+1],",");
			for(int j = 0; j < numberOfWorkers + 3; j++)
			{
				if(st.hasMoreTokens())
				{
					matrixToFill[i][j] = Integer.parseInt(st.nextToken(","));
				}
			}
		}
		
		return matrixToFill;
	}
}
