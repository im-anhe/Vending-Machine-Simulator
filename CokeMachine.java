/*
 * Anne Nguyen
 * Coke Vending Machine Simulator
 */

package CokeVendingMachine;

public class CokeMachine
{
    private String machineName;
    private int changeLevel;
    private int maxChangeCapacity = 5000;
    private int inventoryLevel;
    private int maxInventoryCapacity = 100;
    private int CokePrice;
    private int changeDispensed;
    private static int numberOfCokesSold = 0; 
    
    public enum ACTION
    {
        DISPENSECHANGE, INSUFFICIENTCHANGE, INSUFFICIENTFUNDS, EXACTPAYMENT, NOINVENTORY
    }
    
    public CokeMachine(String name, int cost, int change, int inventory)
    {
        machineName = name;
        CokePrice = cost;
        changeLevel = change;
        inventoryLevel = inventory;
    }
    public CokeMachine()
    {
        machineName = "New Machine";
        CokePrice = 50;
        changeLevel = 500;
        inventoryLevel = 100;
    }
    public String getMachineName()
    {
        return machineName;
    }
    public int getChangeLevel()
    {
        return changeLevel;
    }
    public int getMaxChangeCapacity()
    {
        return maxChangeCapacity;
    }
    public int getInventoryLevel()
    {
        return inventoryLevel;
    }
    public int getMaxInventoryLevel()
    {
        return maxInventoryCapacity;
    }
    public int getCokePrice()
    {
        return CokePrice;
    }
    public int getChangeDispensed()
    {
        return changeDispensed;
    }
    public static int getNumberOfCokesSold()
    {
        return numberOfCokesSold;
    }
    
    public void setMachineName(String newMachineName)
    {
        machineName = newMachineName;
    }
    public void setCokePrice(int newCokePrice)
    {
        CokePrice = newCokePrice;
    }
    
    public boolean incrementInventoryLevel(int amountToAdd)
    {
        if(!((amountToAdd + inventoryLevel) > maxInventoryCapacity))
        {
            inventoryLevel += amountToAdd;
            return true;
        }
        return false;
    }
    public boolean incrementChangeLevel(int amountToAdd)
    {
        if(!(amountToAdd + changeLevel > maxChangeCapacity))
        {
            changeLevel += amountToAdd;
            return true;
        }
        return false;
    }
    
    public ACTION buyACoke(int payment)
    {
        ACTION answer = null;
        if(inventoryLevel == 0 || maxChangeCapacity == changeLevel)
        {
            answer = ACTION.NOINVENTORY;
        }
        else if ((payment - CokePrice)>changeLevel)
        {
            answer = ACTION.INSUFFICIENTCHANGE;
        }
        else if (CokePrice>payment)
        {
            answer = ACTION.INSUFFICIENTFUNDS;
        }
        else if (CokePrice==payment)
        {
            inventoryLevel--;
            numberOfCokesSold++;
            changeLevel += payment;
            answer = ACTION.EXACTPAYMENT;
        }
        else
        {
            inventoryLevel--;
            numberOfCokesSold++;
            changeLevel += CokePrice;
            changeDispensed = payment-CokePrice;
            answer = ACTION.DISPENSECHANGE;
        }
        return answer;
    }
    
    @Override
    public String toString()
    {
        return String.format("\n\n%-23s %-18s\n%-23s %-18d\n%-23s %-18d\n%-23s %-18d\n%-23s %-18d\n%-23s %-18d\n%-23s %-18d\n%-23s %-18d\n","Machine Name", machineName,"Current Inventory Level", inventoryLevel,"Current Change Level", changeLevel,"Last Change Dispensed", changeDispensed,"Inventory Capacity", maxInventoryCapacity,"Change Capacity", maxChangeCapacity,"Coke Price", CokePrice,"Cokes Sold", numberOfCokesSold);
    }
}
