
import java.io.IOException;
import java.util.Stack;

public class FrogPath {
    private Pond pond;

    public FrogPath(String filename) {
        try {
            this.pond = new Pond(filename);
        } catch (IOException e) {
            System.out.println("File not found: " + filename);
            e.printStackTrace();
        }
    }

    


    public Hexagon findBest(Hexagon currCell) {
        ArrayUniquePriorityQueue<Hexagon> queue = new ArrayUniquePriorityQueue<>();
        double minPriority = Double.MAX_VALUE;
        Hexagon bestNeighbor = null;
    
        // Check if the current cell is a lily pad checking for both layers
        if (currCell.isLilyPadCell()) {
            // Enqueue neighbors and neighbors of neighbors
            for (int i = 0; i < 6; i++) {
                try {
                    Hexagon neighbor = currCell.getNeighbour(i);
                    if (neighbor != null && !neighbor.isMarked()) {
                        double priority = computePriority(currCell, neighbor);
                       // System.out.println("Neighbor " + neighbor.getID() + " Priority: " + priority);
                        queue.add(neighbor, priority); // Add neighbor to the queue with its priority
    
                        // Enqueue neighbors of neighbor in clockwise order
                        int j = 0;
                        while (j < 2) {
                            try {
                                // Calculate the index
                                int index = (i + j) % 6; 
                                Hexagon neighborOfNeighbor = neighbor.getNeighbour(index);
                                if (neighborOfNeighbor != null && !neighborOfNeighbor.isMarked()) {
                                    double priorityOfNeighborOfNeighbor = computePriority(currCell, neighborOfNeighbor);
                                    //System.out.println("  Neighbor of Neighbor " + neighborOfNeighbor.getID() + " Priority: " + priorityOfNeighborOfNeighbor);
                                    queue.add(neighborOfNeighbor, priorityOfNeighborOfNeighbor);
                                }
                            } catch (InvalidNeighbourIndexException e) {
                                e.printStackTrace();
                            }
                            j++;
                        }
                    }
                } catch (InvalidNeighbourIndexException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // If the current cell is not a lily pad, no second layer
            for (int i = 0; i < 6; i++) {
                Hexagon neighbor = currCell.getNeighbour(i);
                if (neighbor != null && !neighbor.isMarked()) {
                    double priority = computePriority(currCell, neighbor);
                    //System.out.println("Neighbor " + neighbor.getID() + " Priority: " + priority);
                    queue.add(neighbor, priority);
                }
            }
        }
    
        // Peek at the best neighbor with the lowest priority
        if (!queue.isEmpty()) {
            bestNeighbor = queue.peek();
          //  System.out.println("Best Neighbor: " + bestNeighbor.getID());
        } else {
            //System.out.println("No neghs");
            //return null; // Return null to indicate that there is no solution
        }
    
        // Return the best neighbor
        return bestNeighbor;
    }
    
    



    public String findPath() {
        try {
            Stack<Hexagon> stack = new Stack<>();
            // Push the start cell onto the stack
            stack.push(pond.getStart()); 
            // Mark the start cell as visited
            pond.getStart().markInStack(); 
            // number of flies eaten at the start (0)
            int fliesEaten = 0; 
            StringBuilder pathBuilder = new StringBuilder(); // StringBuilder to construct the path
        
            while (!stack.isEmpty()) {
                Hexagon currentCell = stack.peek(); // Get the current cell from the top of the stack
                pathBuilder.append(currentCell.getID()).append(" "); // Append
    
                //System.out.println("Current Stack: " + stack);
    
                if (currentCell.isEnd()) {
                    // Reached the end cell
                  
                        pathBuilder.append("ate ").append(fliesEaten).append(" flies"); // Append flies eaten to the pathBuilder
                    return pathBuilder.toString(); // Return the path
                }
    
                // Find the best neighbor to move to from the current cell
                Hexagon nextCell = findBest(currentCell);
                if (nextCell != null) {
                    // A valid next move is available
                    if (computePriority(currentCell, nextCell) >=10) {
                        // Check if the priority of the next cell is above 10
                        // Backtrack to the previous cell
                        stack.pop().markOutStack(); // Pop the current cell from the stack and mark it as unvisited
                        if (!stack.isEmpty()) {
                            // If stack is not empty, push the previous cell again for backtracking
                            stack.push(stack.pop());
                        }
                    } else {
                        if (nextCell instanceof FoodHexagon) {
                            // If the next cell is a food (fly) cell
                            int numFlies = ((FoodHexagon) nextCell).getNumFlies();
                            fliesEaten += numFlies; // Add flies to fliesEaten
                            ((FoodHexagon) nextCell).removeFlies(); // Remove flies from the cell
                        }
    
                        stack.push(nextCell); // Push the next cell onto the stack
                        nextCell.markInStack(); // Mark the next cell as visited
                    }
                } else {
                    // No valid move available, backtrack
                    stack.pop().markOutStack(); // Pop the current cell from the stack and mark it as unvisited
                }
            }
    
            // No solution found
            return "No solution";
        } catch (Exception e) {
            return "No solution ";
        }
    }
    
    
    
    
    
    
    
    
    
    private double computePriority(Hexagon currCell, Hexagon neighbor) {
        double priority = 10; // Initial priority based on neighbor's type
    
        if (neighbor instanceof FoodHexagon) {
            FoodHexagon foodNeighbor = (FoodHexagon) neighbor;
            int numFlies = foodNeighbor.getNumFlies();
            if (numFlies == 1) {
                priority = 2.0;
            } else if (numFlies == 2) {
                priority = 1.0;
            } else if (numFlies == 3) {
                priority = 0.0;
            }
        } else if (neighbor.isEnd()) {
            priority = 3.0;
        } else if (neighbor.isLilyPadCell()) {
            priority = 4.0;
        } else if (neighbor.isReedsCell()) {
            priority = 5.0;
        } else if (neighbor.isWaterCell()) {
            priority = 6.0;
        }
    
        // Check if any neighboring cell of the neighbor is an Alligator
        for (int i = 0; i < 6; i++) {
            try {
                Hexagon neighborNeighbor = neighbor.getNeighbour(i);
                if (neighborNeighbor != null && neighborNeighbor.isAlligator()) {
                    priority += 10.0; // Add 10.0 if an Alligator is nearby
                    break;
                }
            } catch (InvalidNeighbourIndexException e) {
                e.printStackTrace();
            }
        }
    //sadly straightline has issues that I couldnt fix
        boolean straightLine = false;
        for (int i = 0; i < 6; i++) {
            try {
                Hexagon neighborNeighbor1 = neighbor.getNeighbour(i);
                if (neighborNeighbor1 != null) {
                    for (int j = 0; j < 2; j++) {
                        try {
                            Hexagon neighborNeighbor2 = neighborNeighbor1.getNeighbour(j);
                            if (neighborNeighbor2 != null && j == i) {
                                // Check if the second layer neighbors are in a straight line with the current neighbor
                                if (neighborNeighbor2.getID() == neighbor.getID()) {
                                    straightLine = true;
                                    break;
                                }
                            }
                        } catch (InvalidNeighbourIndexException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (InvalidNeighbourIndexException e) {
                e.printStackTrace();
            }
        }


        // Adjust priority based on the straight line condition
        if (straightLine) {
            priority += 0.5;
        } else {
            priority += 1.0;
        }
        
    
        return priority;
    }

}


    
    
    









