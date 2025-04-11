package FitnessFunction;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.*;

import java.text.DecimalFormat;
import java.util.*;

public class GAPsoScheduling {
  static Datacenter[] datacenter;
  private static List<Vm> vmlist;
  private static List<Cloudlet> cloudletList;
  private static final GAPSO GPsoSchedulerInstance = new GAPSO();

  public static void main(String[] args) {
    // Step 1: Initialize matrices
    SchedulerForGAPso.initMatrices();

    // Step 2: Run GAPSO to get mapping
    int[] mapping = GPsoSchedulerInstance.run();
    double[][] execTimeMatrix = GPsoSchedulerInstance.getExecTimeMatrix();
    double[][] communTimeMatrix = GPsoSchedulerInstance.getCommunTimeMatrix();

    Log.printLine("Starting G&PSO Task Scheduling Simulation...");

    try {
      int num_user = 1;
      Calendar calendar = Calendar.getInstance();
      boolean trace_flag = false;

      CloudSim.init(num_user, calendar, trace_flag);

      datacenter = new Datacenter[Constant.NO_OF_DATA_CENTERS];
      for (int i = 0; i < Constant.NO_OF_DATA_CENTERS; i++) {
        datacenter[i] = createDatacenter("Datacenter_" + i);
      }

      DatacenterBroker broker = createBroker();
      int brokerId = broker.getId();

      vmlist = new ArrayList<>();
      int mips = 100;
      long size = 10000;
      int ram = 512;
      long bw = 1000;
      int pesNumber = 1;
      String vmm = "Xen";

      for (int i = 0; i < Constant.NO_OF_DATA_CENTERS; i++) {
        vmlist.add(new Vm(i, brokerId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared()));
      }

      broker.submitVmList(vmlist);

      cloudletList = new ArrayList<>();
      long fileSize = 300;
      long outputSize = 300;
      UtilizationModel utilizationModel = new UtilizationModelFull();

      for (int i = 0; i < Constant.NO_OF_TASKS; i++) {
        Cloudlet cloudlet = new Cloudlet(i,
            (int) (1e3 * (execTimeMatrix[i][mapping[i]] + communTimeMatrix[i][mapping[i]])),
            pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
        cloudlet.setUserId(brokerId);
        cloudlet.setVmId(mapping[i]);
        cloudletList.add(cloudlet);
      }

      broker.submitCloudletList(cloudletList);

      CloudSim.startSimulation();
      List<Cloudlet> newList = broker.getCloudletReceivedList();
      CloudSim.stopSimulation();

      printCloudletList(newList);
      Log.printLine("G&PSO CloudSim simulation finished!");

    } catch (Exception e) {
      System.out.println("An error has occurred!\n" + e.getMessage());
    }
  }

  private static void printCloudletList(List<Cloudlet> list) {
    int size = list.size();
    Cloudlet cloudlet;
    String indent = "    ";
    Log.printLine();
    Log.printLine("========== OUTPUT ==========");
    Log.printLine("Cloudlet ID" + indent + "STATUS" + indent +
        "Data center ID" + indent + "VM ID" + indent + "Time" + indent +
        "Start Time" + indent + "Finish Time");

    double mxFinishTime = 0;
    DecimalFormat dft = new DecimalFormat("###.##");

    for (int i = 0; i < size; i++) {
      cloudlet = list.get(i);
      Log.print(indent + cloudlet.getCloudletId() + indent + indent);
      if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
        Log.print("SUCCESS");
        Log.printLine(indent + indent + cloudlet.getResourceId() + indent + indent +
            indent + cloudlet.getVmId() + indent + indent + dft.format(cloudlet.getActualCPUTime()) +
            indent + indent + dft.format(cloudlet.getExecStartTime()) +
            indent + indent + dft.format(cloudlet.getFinishTime()));
        mxFinishTime = Math.max(mxFinishTime, cloudlet.getFinishTime());
      }
    }

    GPsoSchedulerInstance.printBestFitness();
    System.out.println("Final Makespan: " + mxFinishTime);
  }

  private static Datacenter createDatacenter(String name) {
    List<Host> hostList = new ArrayList<>();
    List<Pe> peList = new ArrayList<>();
    int mips = 100;
    peList.add(new Pe(0, new PeProvisionerSimple(mips)));

    int hostId = 0;
    int ram = 2048;
    long storage = 1000000;
    int bw = 10000;

    hostList.add(new Host(hostId,
        new RamProvisionerSimple(ram),
        new BwProvisionerSimple(bw),
        storage, peList,
        new VmSchedulerSpaceShared(peList)));

    String arch = "x86";
    String os = "Linux";
    String vmm = "Xen";
    double time_zone = 10.0;
    double cost = 3.0;
    double costPerMem = 0.05;
    double costPerStorage = 0.001;
    double costPerBw = 0.0;

    LinkedList<Storage> storageList = new LinkedList<>();
    DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
        arch, os, vmm, hostList, time_zone, cost,
        costPerMem, costPerStorage, costPerBw);

    Datacenter datacenter = null;
    try {
      datacenter = new Datacenter(name, characteristics,
          new VmAllocationPolicySimple(hostList), storageList, 0);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return datacenter;
  }

  private static DatacenterBroker createBroker() {
    DatacenterBroker broker = null;
    try {
      broker = new DatacenterBroker("Broker");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return broker;
  }
}
