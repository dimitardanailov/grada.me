package libraries;

import java.io.IOException;
import java.io.RandomAccessFile;

import android.util.Log;

public class MemoryTracker {

    public static final String MEM_TOTAL 		= "MemTotal";
    public static final String MEM_FREE 		= "MemFree";
    public static final String BUFFERS 			= "Buffers";
    public static final String CACHED 			= "Cached";
    public static final String SWAP_CACHED 		= "SwapCached";
    public static final String ACTIVE 			= "Active";
    public static final String INACTIVE 		= "Inactive";
    public static final String HIGH_TOTAL 		= "HighTotal";
    public static final String HIGH_FREE 		= "HighFree";
    public static final String LOW_TOTAL 		= "LowTotal";
    public static final String LOW_FREE 		= "LowFree";
    public static final String SWAP_TOTAL 		= "SwapTotal";
    public static final String SWAP_FREE 		= "SwapFree";
    public static final String DIRTY 			= "Dirty";
    public static final String WRITE_BACK 		= "Writeback";
    public static final String MAPPED 			= "Mapped";
    public static final String SLAB 			= "Slab";
    public static final String COMMITTED_AS 	= "Committed_AS";
    public static final String PAGE_TABLES 		= "PageTables";
    public static final String VMALLOCTOTAL 	= "VmallocTotal";
    public static final String VMALLOCUSED 		= "VmallocUsed";
    public static final String VMALLOCCHUNK 	= "VmallocChunk";
    public static final String HUGE_PAGES_TOTAL = "HugePages_Total";
    public static final String HUGE_PAGES_FREE 	= "HugePages_Free";
    public static final String HUGE_PAGE_SIZE 	= "Hugepagesize";

    // Get Class Name
    private static String TAG = MemoryTracker.class.getName();

    // Store information for device memory
    private long totalDeviceMemory = 0;

    // Device memory can't be higher from maximumPercentDeviceUsage value
    // We can memory usage can be MemTotal * maximumPercentDeviceUsage
    private double maximumPercentDeviceUsage = 0.85f;

    /*** Getter and Setter - Total Device Memory ***/
    public long getTotalDeviceMemory() {
        return totalDeviceMemory;
    }

    public void setTotalDeviceMemory(long totalDeviceMemory) {
        this.totalDeviceMemory = totalDeviceMemory;
    }
    /*** Getter and Setter - Total Device Memory ***/

    /*** Getter and Setter - Maximum Percent Device Usage ***/
    public double getMaximumPercentDeviceUsage() {
        return maximumPercentDeviceUsage;
    }

    public void setMaximumPercentDeviceUsage(double maximumPercentDeviceUsage) {
        this.maximumPercentDeviceUsage = maximumPercentDeviceUsage;
    }
    /*** Getter and Setter - Maximum Percent Device Usage ***/

    /**
     * Use RandomAccessFile class to get information for device memory
     * Documentation: http://www.centos.org/docs/5/html/5.2/Deployment_Guide/s2-proc-meminfo.html
     *
     * User can filter information for:
     *
     * MemTotal — Total amount of physical RAM, in kilobytes.
     *
     * MemFree — The amount of physical RAM, in kilobytes, left unused by the system.
     *
     * Buffers — The amount of physical RAM, in kilobytes, used for file buffers.
     *
     * Cached — The amount of physical RAM, in kilobytes, used as cache memory.
     *
     * SwapCached — The amount of swap, in kilobytes, used as cache memory.
     *
     * Active — The total amount of buffer or page cache memory, in kilobytes, that is in active use.
     * This is memory that has been recently used and is usually not reclaimed for other purposes.
     *
     * Inactive — The total amount of buffer or page cache memory, in kilobytes, that are free and available.
     * This is memory that has not been recently used and can be reclaimed for other purposes.
     *
     * HighTotal and HighFree — The total and free amount of memory, in kilobytes, that is not directly mapped into kernel space.
     * The HighTotal value can vary based on the type of kernel used.
     *
     * LowTotal and LowFree — The total and free amount of memory, in kilobytes, that is directly mapped into kernel space.
     * The LowTotal value can vary based on the type of kernel used.
     *
     * SwapTotal — The total amount of swap available, in kilobytes.
     *
     * SwapFree — The total amount of swap free, in kilobytes.
     *
     * Dirty — The total amount of memory, in kilobytes, waiting to be written back to the disk.
     *
     * Writeback — The total amount of memory, in kilobytes, actively being written back to the disk.
     *
     * Mapped — The total amount of memory, in kilobytes, which have been used to map devices, files, or libraries using the mmap command.
     *
     * Slab — The total amount of memory, in kilobytes, used by the kernel to cache data structures for its own use.
     *
     * Committed_AS — The total amount of memory, in kilobytes, estimated to complete the workload.
     * This value represents the worst case scenario value, and also includes swap memory.
     *
     * PageTables — The total amount of memory, in kilobytes, dedicated to the lowest page table level.
     *
     * VMallocTotal — The total amount of memory, in kilobytes, of total allocated virtual address space.
     *
     * VMallocUsed — The total amount of memory, in kilobytes, of used virtual address space.
     *
     * VMallocChunk — The largest contiguous block of memory, in kilobytes, of available virtual address space.
     *
     * HugePages_Total — The total number of hugepages for the system. The number is derived by dividing
     *
     * Hugepagesize by the megabytes set aside for hugepages specified in /proc/sys/vm/hugetlb_pool.
     * This statistic only appears on the x86, Itanium, and AMD64 architectures.
     *
     * HugePages_Free — The total number of hugepages available for the system.
     * This statistic only appears on the x86, Itanium, and AMD64 architectures.
     *
     * Hugepagesize — The size for each hugepages unit in kilobytes. By default,
     * the value is 4096 KB on uniprocessor kernels for 32 bit architectures.
     * For SMP, hugemem kernels, and AMD64, the default is 2048 KB. For Itanium architectures,
     * the default is 262144 KB. This statistic only appears on the x86, Itanium, and AMD64 architectures.
     *
     * @return
     */
    public long accessDeviceProcMemInfoFilterByKeyName(String filterParam) {
        RandomAccessFile reader = null;
        long memoryInfo = 0;

        try {
            reader = new RandomAccessFile("/proc/meminfo", "r");

            for ( String line; (line=reader.readLine()) != null;) {

                if (line.contains(filterParam)) {

                    // Log.d(TAG, "{MemoryTracker}: " + line + ", filterParam: " + filterParam);

                    // Try to Convert string to double
                    if (!line.isEmpty()) {
                        // Extract digits from a string in Java
                        line = line.replaceAll("\\D+","");
                        line = line.trim();
                        try {
                            memoryInfo = Long.parseLong(line);
                        } catch (NumberFormatException ex) {
                            Log.e(TAG,"MemoryTracker IOException exception.");
                            ex.printStackTrace();
                        }
                    }

                    break;
                }
            }

            reader.close();

        } catch (IOException ex)  {
            Log.e(TAG,"MemoryTracker IOException exception.");
            ex.printStackTrace();
        }

        return memoryInfo;
    }

    /**
     * Get information for MemFree, Buffers and Cached.
     * Available Device Memory is equal to: (MemFree + Buffers + Cached)
     * Post: http://stackoverflow.com/questions/3019748/how-to-reliably-measure-available-memory-in-linux
     * @return availableMemory
     */
    public long getAvailableMemoryDeviceMemory() {
        long freeMemory = accessDeviceProcMemInfoFilterByKeyName(MemoryTracker.MEM_FREE);
        long buffers = accessDeviceProcMemInfoFilterByKeyName(MemoryTracker.BUFFERS);
        long cached = accessDeviceProcMemInfoFilterByKeyName(MemoryTracker.CACHED);

        long availableMemory = freeMemory + buffers + cached;

        return availableMemory;
    }

    /**
     * Method calculate memory usage (MemTotal - (MemFree + Buffers + Cached))
     * If percent device usage is lower from maximumPercentDeviceUsage, we have memory for operations
     * @return
     */
    public Boolean checkDeviceMemoryUsageIsAvailable() {
        if (totalDeviceMemory > 0) {
            long availableMemory = getAvailableMemoryDeviceMemory();
            double memoryUsage = totalDeviceMemory - availableMemory;
            double percentDeviceMemoryUsage = (memoryUsage / (double) totalDeviceMemory);

            Log.d(TAG, "Total Device memory is equal to: " + totalDeviceMemory);
            Log.d(TAG, "Available Device Memory is equal to: " + availableMemory);
            Log.d(TAG, "Memory Usage is equal to: " + memoryUsage);
            Log.d(TAG, "Percent is equal to: " + percentDeviceMemoryUsage);
            Log.d(TAG, "---------");

            if (maximumPercentDeviceUsage >= percentDeviceMemoryUsage) {
                // We have available memory for operations
                return true;
            } else {
                // Device memory is too higher for another operations
                return false;
            }
        } else {
            return false;
        }
    }
}