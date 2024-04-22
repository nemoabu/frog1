import java.util.Arrays;

public class ArrayUniquePriorityQueue<T> implements UniquePriorityQueueADT<T> {
    private T[] queue;
    private double[] priority;
    private int count;

    public ArrayUniquePriorityQueue() {
        queue = (T[]) new Object[10];
        priority = new double[10];
        count = 0;
    }

    public void add(T data, double prio) {
        if (contains(data)) {
            return;
        }
    
        if (count == queue.length) {
            queue = Arrays.copyOf(queue, queue.length + 5);
            priority = Arrays.copyOf(priority, priority.length + 5);
        }
    
        int insertIndex = count; // Initialize the insert index to the end of the queue
    
        // Find the correct position to insert the new element based on its priority
        while (insertIndex > 0 && prio < priority[insertIndex - 1]) {
            // Move elements to the right until we find the correct position
            queue[insertIndex] = queue[insertIndex - 1];
            priority[insertIndex] = priority[insertIndex - 1];
            insertIndex--;
        }
    
        // Insert the new element 
        queue[insertIndex] = data;
        priority[insertIndex] = prio;
        count++;
    }
    

    public boolean contains(T data) {
        for (int currentIndex = 0; currentIndex < count; currentIndex++) {
            if (queue[currentIndex].equals(data)) {
                return true;
            }
        }
        return false;
    }
    

    public T peek() throws CollectionException {
        if (isEmpty()) {
            throw new CollectionException("PQ is empty");
        }
        return queue[0];
    }

    public boolean isEmpty() {
        return count == 0;
    }
    

    public void updatePriority(T data, double newPriority) throws CollectionException {
            boolean found = false;
        for (int i = 0; i < count; i++) {
            if (queue[i].equals(data)) {
                this.priority[i] = newPriority;
                found = true;
                break;
            }
        }
    
        if (!found) {
            throw new CollectionException("Item not found in PQ");
        }
    
        // Rearanging all
  
        for (int current = 0; current < count - 1; current++) {
            for (int next = current + 1; next < count; next++) {
                if (priority[current] > priority[next] || 
                    (priority[current] == priority[next])) {
                    // Swap elements and priorities
                    T tempData = queue[current];
                    double tempPriority = priority[current];
                    queue[current] = queue[next];
                    priority[current] = priority[next];
                    queue[next] = tempData;
                    priority[next] = tempPriority;
                }
            }
        }
    }
        
    
    
    

    public T removeMin() throws CollectionException {
        if (isEmpty()) {
            throw new CollectionException("PQ is empty");
        }
    
        // Find the index of the minimum priority element
        int minIndex = 0;
        double minPriority = priority[0];
        for (int i = 1; i < count; i++) {
            if (priority[i] < minPriority) {
                minIndex = i;
                minPriority = priority[i];
            }
        }
    
        T min = queue[minIndex];
    
        // Shift subsequent values to the left to fill the gap
        for (int i = minIndex; i < count - 1; i++) {
            queue[i] = queue[i + 1];
            priority[i] = priority[i + 1];
        }
    
        // Decrement count
        count--;
    
        return min;
    }
    
    
    

    public int size() {
        return count;
    }
    public int getLength() {
        return queue.length; // or priority.length, as they should have the same capacity
    }

    public String toString() {
        if (isEmpty()) {
            return "The PQ is empty";
        } else {
            StringBuilder result = new StringBuilder();
            for (int currentIndex = 0; currentIndex < count; currentIndex++) {
                result.append(queue[currentIndex]).append(" [").append(priority[currentIndex]).append("]");
                if (currentIndex < count - 1) {  // Check if it's not the last element
                    result.append(", ");
                }
            }
            return result.toString();
        }
    }
    
}