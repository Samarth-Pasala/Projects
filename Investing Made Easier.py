#!/usr/bin/env python
# coding: utf-8

# In[2]:


#!/usr/bin/env python
# coding: utf-8

# In[18]:


#%%

import pandas as pd
import datetime as dt
import numpy as np

#you need to install these two 
#!pip install yfinance
#!pip install matplotlib

import matplotlib.pyplot as plt
import yfinance as yf   


start = dt.datetime(2010, 1, 1)
end = dt.datetime.now()

#this calculates the number of months of the prices you pull
months = (end.year - start.year) * 12 + (end.month - start.month)


#you can preload the stocks up here
AAPL = yf.download('AAPL',start,end) 
MSFT = yf.download('MSFT',start,end) 
GOOG = yf.download('GOOG',start,end) 
F = yf.download('F',start,end)
VBISX = yf.download('VBISX',start,end) 
VFIAX = yf.download('VFIAX',start,end)


a = input("How much money do you think you can invest?")
b = input("How okay are you with risk on a scale of 1-100?")
c = input("Do you care more about maximizing the value of your money? (short-term) or protecting the value of your money (long-term)? (Please type short-term or long-term)")

print()

# if b=="long":
#     time= 10
    
# if b=="short":
#     time= 5

if c == "long-term":
    print("You should invest in Microsoft, Google, and Apple!")
    
    Stock=MSFT
    Title="Microsoft Stock Price"
    
#     print("You should invest in Google")
    Stock2 = GOOG
    Title2 = "Google Stock Price"
    
#     print("You should invest in Apple")
    Stock3 = AAPL
    Title3 = "Apple Stock Price"
    
    #this creates the plot for the stock prices (Option 1)
    Stock.Close.plot() 
    plt.title(Title)
    plt.ylabel('price')
    plt.xlabel('Date')
    plt.show()

    Stock2.Close.plot() 
    plt.title(Title2)
    plt.ylabel('price')
    plt.xlabel('Date')
    plt.show()

    Stock3.Close.plot() 
    plt.title(Title3)
    plt.ylabel('price')
    plt.xlabel('Date')
    plt.show()
    
    #this code creates a average returns for each stock over a year
    total_return = (Stock.Close[-1] - Stock.Close[0])/Stock.Close[0]
    annualized_return=((1 + total_return)**(12/months))-1

    total_return2 = (Stock2.Close[-1] - Stock2.Close[0])/Stock2.Close[0]
    annualized_return2 =((1 + total_return2)**(12/months))-1

    total_return3 = (Stock3.Close[-1] - Stock3.Close[0])/Stock3.Close[0]
    annualized_return3 =((1 + total_return3)**(12/months))-1
    
    investment= float(a)
    
    print("If you invest in Microsoft, your expected return is:", annualized_return*100,"% per year")

    print("If you invest in Google, your expected return is:", annualized_return2*100,"% per year")

    print("If you invest in Apple, your expected return is:", annualized_return3*100,"% per year")
    
    print()
    
    time = 5
    time1 = 10
    money = investment*(1+annualized_return)**time
    money2 = investment*(1+annualized_return2)**time
    money3 = investment*(1+annualized_return)**time1
    money4 = investment*(1+annualized_return2)**time1

    #Apple
    money5 = investment*(1+annualized_return3)**time
    money6 = investment*(1+annualized_return3)**time1

    print("If you invest in Microsoft, you should have $",money,"after", time, "years. If you invest for", time1, "years, you should have $", money3)
    print()
    
    # print ("If you invest in Microsoft, you should have $",money,"after", time , "years")
    print ("If you invest in Google, you should have $",money2,"after", time , "years. If you invest for", time1, "years, you should have $", money4)
    print()
    
    #Apple
    print("If you invest in Apple, you should have $",money5,"after", time, "years. If you invest for", time1, "years, you should have $", money6)


#put if statements here
risk1 = int(b)
if risk1 < int(50) and c == "short-term":
    print("Based on the information provided, you should invest more in bonds because it is much less of a risk and you will still maximize your profit!")
print()

# option 1 = Apple and option 2 = Microsoft
if c == "short-term":
    
    print("You should invest in Ford Motor Company, the Vanguard 500 Index Fund Admiral Shares, or Vanugard Short-Term Bond Index Fund Investor Shares!")
    
    Stock4 = F
    Title4 = "Ford Stock Price"
    
#     print("You should invest in Vanguard 500 Index Fund Admiral Shares")
    Stock5 = VFIAX
    Title5 = "Vanguard 500 Index Fund Admiral Shares"
    
#     print("You should invest in VBISX")
    Stock6 = VBISX
    Title6 = "Vanguard Short-Term Bond Index Fund Investor Shares"

    #Option 2
    Stock4.Close.plot() 
    plt.title(Title4)
    plt.ylabel('price')
    plt.xlabel('Date')
    plt.show()

    Stock5.Close.plot() 
    plt.title(Title5)
    plt.ylabel('price')
    plt.xlabel('Date')
    plt.show()
    
    Stock6.Close.plot() 
    plt.title(Title6)
    plt.ylabel('price')
    plt.xlabel('Date')
    plt.show()


#These two are another way to calculate stock returns
#Stock['log_price'] = np.log(Stock.Close)
#Stock['log_return'] = Stock['log_price'].diff(1)

    #Option 2
    total_return4 = (Stock4.Close[-1] - Stock4.Close[0])/Stock4.Close[0]
    annualized_return4 =((1 + total_return4)**(12/months))-1

    total_return5 = (Stock5.Close[-1] - Stock5.Close[0])/Stock5.Close[0]
    annualized_return5 =((1 + total_return5)**(12/months))-1
    
    total_return6 = (Stock6.Close[-1] - Stock6.Close[0])/Stock6.Close[0]
    annualized_return6 =((1 + total_return6)**(12/months))-1

    #these lines calculate the investments return and those them to the user, and the total amount
    #after the time period they choose

    investment= float(a)


    #Option 2
    print("If you invest in Ford Motor Company, your expected return is:", annualized_return4*100,"% per year")

    print("If you invest in the Vanguard 500 Index Fund Admiral Shares, your expected return is:", annualized_return5*100,"% per year")
    
    print("If you invest in the Vanugard Short-Term Bond Index Fund Investor Shares, your expected return is:", annualized_return5*100,"% per year")
    
    print()


    time = 5
    time1 = 10


    #Ford Motor Company
    money7 = investment*(1+annualized_return4)**time
    money8 = investment*(1+annualized_return4)**time1

    money9 = investment*(1+annualized_return5)**time
    money10 = investment*(1+annualized_return5)**time1
    
    money11 = investment*(1+annualized_return6)**time
    money12 = investment*(1+annualized_return6)**time1

    #Ford Motor Company
    print("If you invest in Ford Motor Company, you should have $",money7,"after", time, "years. If you invest for", time1, "years, you should have $", money8)
    print()
    
    #Vanguard 500 Index Fund Admiral Shares
    print("If you invest in Vanguard 500 Index Fund Admiral Shares, you should have $",money9,"after", time, "years. If you invest for", time1, "years, you should have $", money10)
    print()
    
    #VBISX
    print("If you invest in Vanugard Short-Term Bond Index Fund Investor Shares, you should have $",money11,"after", time, "years. If you invest for", time1, "years, you should have $", money12)


# In[ ]:




