# Task-Allocator-using-Maximum-Bipartite-Matching
You are given a set of n tasks and m resources (or workers). Each task can be completed by a single worker. For each task, the subset of workers qualified to perform it are also provided to you. Your objective is to find an assignment of tasks to people such that as many jobs are completed. Model this as a maximum bipartite graph matching problem and solve it.

Input Format:   
Your input will be a single file of the following format. The first line contains the total number of tasks and the number of workers separated by a space. 
Followed by a tuples \<task id, list of qualified worker ids>.

**Sample Input:**   
5 6   
1,6,2,5,1   
2,1,3   
3,3,4   
4,4,6   
5,4,5,2   

**Corresponding Output:**   
TaskID 1 : Worker ID 1    
TaskID 2 : Worker ID 3    
TaskID 3 : Worker ID 4    
TaskID 4 : Worker ID 6    
TaskID 5 : Worker ID 5    

**References:**   
- http://www.sanfoundry.com/java-program-implement-ford-fulkerson-algorithm/    
- http://algs4.cs.princeton.edu/64maxflow/FordFulkerson.java.html   
- http://www.geeksforgeeks.org/ford-fulkerson-algorithm-for-maximum-flow-problem/   
- http://www.geeksforgeeks.org/breadth-first-traversal-for-a-graph/
