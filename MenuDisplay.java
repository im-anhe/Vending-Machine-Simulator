/*
 * Anne Nguyen
 * Coke Vending Machine Simulator
 */
package CokeVendingMachine;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class MenuDisplay
{
    public static int CokeMenu(String name)
    {
        Scanner scan = new Scanner(System.in);
        int userInput = -1;
        
        while (userInput < 0 || userInput >6)
        {
            System.out.println("\n\n" + name + "\n\n");
            System.out.println("0. Walk away\n1. Buy a Coke\n2. Restock Machine");
            System.out.println("3. Add change\n4. Display Machine Info");
            System.out.println("5. Update Machine Name\n6. Update Coke price");
            System.out.print("\nChoice? ");
            try
            {
                userInput = scan.nextInt();
            }
            catch(Exception e)
             {
                userInput = -1;
                scan.nextLine();
            }

            if (userInput < 0 || userInput >6)
            {
                System.out.println("\nInvalid menu choice. Please choose again.");
            }
        }
        return userInput;
    }
    
    public static int newMenuDisplay(ArrayList <CokeMachine> CokeMachines)
    {
        Scanner scan = new Scanner(System.in);
        int userInput = -1;
        
        
        while (userInput < 0 || userInput >CokeMachines.size()+1)
        {
            System.out.print("\n\nPick a Coke Machine\n\n\n");
            System.out.print("0. Exit");
            for(int i = 0; i<CokeMachines.size(); i++)
            {
                System.out.print("\n" + (i+1) + ". " + CokeMachines.get(i).getMachineName());
            }
            System.out.print("\n" + (CokeMachines.size()+1) +". Add new machine\n\nChoice? ");
            
            try
            {
                userInput = scan.nextInt();
            }
            catch(Exception e)
             {
                userInput = -1;
                scan.nextLine();
            }

            if (userInput < 0 || userInput > CokeMachines.size()+1)
            {
                System.out.println("\nInvalid menu choice. Please choose again.");
            }
        }
        return userInput;
    }
    
    public static String displayMoney(int change)
    {
        String dollars = String.valueOf(change/100);
        String cents = String.valueOf(change%100);
        return "$" + dollars + "." + (cents.length() == 1 ? "0" : "") + cents;
    }
    
    public static void ReadFile(String filename, ArrayList <CokeMachine> CokeMachines)
    {
        File FH = new File(filename);
        Scanner inFile = null; 
        
        try
        {
            inFile = new Scanner(FH);
        }
        catch(Exception e)
        {
            //doesnt work
            System.out.println(filename + " file name does not exist...exiting");
            System.exit(0);
        }
        
        String first;
        
        while(inFile.hasNextLine())
        {
            
            first = inFile.nextLine();
            String info[] = first.split("[|]");
            CokeMachines.add(new CokeMachine(info[0], Integer.parseInt(info[1]), Integer.parseInt(info[2]), Integer.parseInt(info[3]))); 
        }
        
        inFile.close();
    }
    
    
    public static void outputFile(String outputFilename, ArrayList <CokeMachine> CokeMachines)throws FileNotFoundException
    {
        PrintWriter out = null;
        
        try
        {
            out = new PrintWriter(outputFilename);
            
        }
        catch(FileNotFoundException e)
        {
            System.out.println("Unable to write output file");
            //print exception object
            System.out.println(e.getMessage());
            //throw exception objext
            throw new FileNotFoundException();
        }
        
        //enhanced for loop to print each CokeMachine's information to file
        for(CokeMachine sets : CokeMachines)
        {
            out.printf("%s|%d|%d|%d\n",sets.getMachineName(), sets.getCokePrice(), sets.getChangeLevel(), sets.getInventoryLevel());
        }
        out.close();
    }
    
    
    public static void main(String[] args) throws FileNotFoundException
    {
        //args length is not 2, exit
        if(args.length != 2)
        {
            System.out.println("\"Missing command line parameters – - Usage : INPUTFILENAME= OUTPUTFILENAME=\"");
            System.exit(0);
        }
        String inputFileName = args[0].substring(args[0].indexOf("=")+1);
        String outputFileName = args[1].substring(args[1].indexOf("=")+1);
        
        ArrayList <CokeMachine> SetOfCokeMachines = new ArrayList<>();
        
        ReadFile(inputFileName, SetOfCokeMachines);
        
        int choices;
        //String name;
        Scanner heow = new Scanner(System.in);
        
        do
        {
            choices = newMenuDisplay(SetOfCokeMachines);
            
            int choice;
            
            if(choices == 0)
            {
                //print out how many cokes sold and program finishes
                System.out.printf("%d Coke(s) were sold today!\n", CokeMachine.getNumberOfCokesSold());
                outputFile(outputFileName, SetOfCokeMachines);
            }
            else if(choices == SetOfCokeMachines.size()+1)
            {
                SetOfCokeMachines.add(new CokeMachine());
            }
            else
            {
                //to enter every coke machine
                do
                {
                    choices-=1;
                    choice = CokeMenu(SetOfCokeMachines.get(choices).getMachineName());
                    switch(choice)
                    {
                        case 0 :
                            if(SetOfCokeMachines.get(choices).getNumberOfCokesSold()==0)
                            {
                                System.out.println("\n\nAre you sure you aren't really THIRSTY and need a Coke?");
                            }
                            else
                            {
                                System.out.println("\n\nBye - enjoy your Coke");
                            }
                            break;
                        case 1 :
                            System.out.println("\nA coke costs " + displayMoney(SetOfCokeMachines.get(choices).getCokePrice()));
                            System.out.print("\nInsert payment ");
                            int payment = heow.nextInt();
                            switch(SetOfCokeMachines.get(choices).buyACoke(payment))
                            {
                                case NOINVENTORY :
                                    System.out.println("\n\nUnable to sell Coke - call 1800WEDONTCARE to register a complaint... returning your payment.");
                                    break;
                                case INSUFFICIENTFUNDS :
                                    System.out.println("\n\nInsufficient payment...returning your payment");
                                    break;
                                case EXACTPAYMENT :
                                    System.out.println("\n\nThank you for exact payment.\nHere's your Coke");
                                    break;
                                case INSUFFICIENTCHANGE :
                                    System.out.println("\n\nUnable to give change at this time...returning your payment");
                                    break;
                                case DISPENSECHANGE :
                                    System.out.println("\n\nHere's your Coke and your change of " + displayMoney(SetOfCokeMachines.get(choices).getChangeDispensed()));
                                    break;                        
                            }
                            break;
                        case 2 :
                            System.out.print("\n\nHow much product are you adding to the machine? ");
                            int add = heow.nextInt();
                            if (SetOfCokeMachines.get(choices).incrementInventoryLevel(add))
                            {
                                System.out.println("\n\nYour machine has been restocked");
                            }
                            else
                            {   
                                System.out.println("\n\nYou have exceeded your machine's max capacity - no inventory was added");
                            }
                            System.out.printf("\nYour inventory level is %d\n", SetOfCokeMachines.get(choices).getInventoryLevel());
                            break;
                        case 3 :
                            System.out.print("\n\nHow much change are you adding to the machine? ");
                            int lad = heow.nextInt();
                            if (SetOfCokeMachines.get(choices).incrementChangeLevel(lad))
                            {
                                System.out.println("\n\nYour change level has been updated");
                            }
                            else
                            {
                                System.out.println("\n\nYou have exceeded your machine's max capacity - no change added");
                            }
                            System.out.printf("\nYour change level is %s with a max capacity of %s", displayMoney(SetOfCokeMachines.get(choices).getChangeLevel()), displayMoney(SetOfCokeMachines.get(choices).getMaxChangeCapacity()));
                            break;
                        case 4 :
                            System.out.println(SetOfCokeMachines.get(choices));
                            break;
                        case 5 :
                            System.out.print("\n\nEnter a new machine name ");
                            String newMachineName = heow.nextLine();
                            SetOfCokeMachines.get(choices).setMachineName(newMachineName);
                            System.out.println("\n\nMachine name has been updated");        
                            break;
                        case 6 :
                            System.out.print("\n\nEnter a new Coke price ");
                            int newCokePrice = heow.nextInt();
                            SetOfCokeMachines.get(choices).setCokePrice(newCokePrice);
                            System.out.println("\n\nCoke price has been updated");
                            break;
                            
                    }
                    choices+=1;
                }
                while(choice != 0 );
            }
        }
        while(choices !=0 );
    }   
}

