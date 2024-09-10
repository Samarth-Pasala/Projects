#!/usr/bin/env python
# coding: utf-8

# In[3]:


from enum import Enum
import csv
filename = './AB_NYC_2019.csv'

home_currency = ''

""" This function in the program asks the user for their name and then
prints out a welcome statement through the concatenation of two strings.
Additionally, it asks the user for their home currency, sets the class 
attribute 'copyright' to a new message, and allows the user to enter a 
name for their header.
"""


def main():
    """ Has three different inputs for the user: obtaining their name,
    assigning a name for their header, and obtaining their home
    currency.
    """
    users_name = input("Please enter your name: ")
    first_string = "Hi " + users_name + ","
    second_string = " welcome to the AirBNB NYC Database."
    complete_statement = first_string + second_string
    print(complete_statement)
    air_bnb = DataSet()
    while air_bnb.header == "":
        users_header = input("Enter a header for the menu: ")
        air_bnb = DataSet(users_header)
        if air_bnb.header == "":
            print("The header must be less than or equal to 30 characters "
                  "long.")
        else:
            continue
    while True:
        global home_currency
        home_currency = input("What is your home currency? ")
        if home_currency in conversions:
            print("Options for converting from", home_currency + ":")
            break
    DataSet.copyright = "copyright 2021 Samarth Pasala"
    menu(air_bnb)


""" This function in the program sets up all of the print statements 
that the user will pick from when called. 
"""


def print_menu():
    """ The options the user will have to pick from. """
    print("Main Menu")
    print("1 - Print Average Rent by Location and Property Type")
    print("2 - Print Minimum Rent by Location and Property Type")
    print("3 - Print Maximum Rent by Location and Property Type")
    print("4 - Load Data")
    print("9 - Quit")


"""An exception that will be raised when a method is asked to do 
calculation on the dataset, but no data is loaded. 
"""


class EmptyDatasetError(Exception):
    """ Passes the error in the class itself but will be raised in
    other areas of the code.
    """
    pass


""" An exception that will be raised when _cross_table_statistics 
does not find any data that matches both descriptors.
"""


class NoMatchingItems(Exception):
    """ Passes the error in the class itself but will be raised in
    other areas of the code.
    """
    pass


""" This class in the function does many things. Firstly, it has a class 
attribute known as copyright, which is set to a string. Additionally, 
It has a header property which does to see if the length of the header 
is less than or equal to 30 characters. The class uses a getter and a
setter to help with the methods. The class has a method which 
sets header equal to header and raises a ValueError if something goes 
wrong. Furthermore, the method sets self._data to None. Also, 
self._labels is initialized in the first method of the class.Two new 
methods in this class are _cross_table_statistics and load_file. 
The _cross_table_statistics method checks for two specific error 
conditions and raises errors if they are true. If everything is correct, 
then the method returns the minimum, average, and maximum rents as a 
tuple of floats. The load_default_data method just sets self._data to a 
table of values as a list of tuples. The Categories and Stats 
method just set members equal to integers. The display_cross_table 
function within the class prints a table of rates based on the borough 
and property type based on whether the user wants the average, minimum, 
or maximum values. 
"""


class DataSet:
    copyright = "No copyright has been set."

    """ Sets self.header equal to itself and raises an exception is the 
    call fails, and also sets self._data to None for the moment. 
    Furthermore, self._labels is initialized.
    """

    def __init__(self, header=""):
        try:
            self.header = header
        except ValueError:
            self._header = ""
        self._data = None
        self._labels = {DataSet.Categories.LOCATION: set(),
                        DataSet.Categories.PROPERTY_TYPE: set()}

    """ This method in the class raises an EmptyDatasetError if no data
    is loaded. Additionally, this functions serves to populate the 
    self._labels dictionary with the appropriate labels from self._data.
    """

    def _initialize_sets(self):
        """ Raises an error if no data is loaded and populates
        self._labels using self._data.
        """
        if self._data is None:
            raise EmptyDatasetError
        else:
            list_of_boroughs = [item[0] for item in self._data]
            list_of_property_types = [item[1] for item in self._data]
            self._labels[DataSet.Categories.LOCATION] = set(list_of_boroughs)
            self._labels[DataSet.Categories.PROPERTY_TYPE] = \
                set(list_of_property_types)

    """ This method in the class loads all of the data from the Airbnb 
    data file into self._data, opens a separate file, and reports how 
    many lines are loaded after some modifications. 
    """
    def load_file(self):
        """ Loads all of the data from the Airbnb data file into
        self._data, opens a separate file, and reports how many lines
        are loaded.
        """
        self._data = []  # Initialize _data as an empty list to store rows
        with open(filename, 'r', newline='') as file:
            csv_reader = csv.reader(file)
            next(csv_reader)  # Skip header row
            csv_list = list(csv_reader)
            print(f"Loaded {len(csv_list)} lines.")  # Report how many lines are loaded

            # Process and store each row as a tuple
            for row in csv_list:
                # Only keep relevant columns and convert rent to integer
                try:
                    location = row[1].strip()
                    property_type = row[2].strip()
                    rent = int(row[3].strip())
                    # Append each row as a tuple to _data
                    self._data.append((location, property_type, rent))
                except ValueError:
                    print(f"Skipping row due to data conversion error: {row}")

        self._initialize_sets()  # Initialize labels based on loaded data


    @property
    def header(self):
        return self._header

    @header.setter
    def header(self, header):
        if len(header) <= 30:
            self._header = header
        else:
            raise ValueError

    """This method in the class checks two specific separate conditions 
    and prints an error is than condition is true. If everything works 
    as it is supposed to, the method will return the minimum, average, 
    and maximum rents as a tuple of floats.
    """

    def _cross_table_statistics(self, descriptor_1: str,
                                descriptor_2: str):
        """ Checks two specific separate conditions and prints an error
        if they are true. Otherwise, if everything is working well, it
        returns the minimum, average, and maximum rents as a tuple of
        floats.

        Args:
            descriptor_1 (str): Represents the borough in the table
            descriptor_2 (str): Represents the property type
        """
        if self._data is None:
            raise EmptyDatasetError
        list_of_rents = [item[2] for item in self._data if
                         descriptor_1 == item[0] and descriptor_2 == item[1]]
        if len(list_of_rents) == 0:
            raise NoMatchingItems
        else:
            return [float(min(list_of_rents)),
                    float((sum(list_of_rents) / len(list_of_rents))),
                    float(max(list_of_rents))]

    """ This method in the class will be used to distinguish between 
    the two categorical variables that are in the dataset.
    """

    class Categories(Enum):
        """ Sets the members equal to values which will be used in
        other parts of the code.
        """
        LOCATION = 0
        PROPERTY_TYPE = 1

    """ Selects a statistic to display in the display_cross_table 
    method.
    """

    class Stats(Enum):
        """ Sets the members equal to values which will be used in
        other parts of the code."""
        MIN = 0
        AVG = 1
        MAX = 2

    """ This method in the class sets up a table of rates for each 
    borough and property type. The table that will be printed is 
    dependent on if the user wants the average, minimum, or maximum 
    values.
    """

    def display_cross_table(self, stat: Stats):
        """ Prints tables of rates depending on if stat is equal to
        the average, minimum, or maximum.

        Args:
            stat (Stats): Used to selected which statistic to display
        """
        copy1 = list(self._labels[DataSet.Categories.PROPERTY_TYPE])
        copy2 = list(self._labels[DataSet.Categories.LOCATION])
        if self._data is None:
            raise EmptyDatasetError
        else:
            header_string = f"{'':<23}"
            for item1 in copy1:
                header_string = f"{header_string}{item1:<34}"
            print(f"{header_string}")
            if stat == stat.MIN:
                for item3 in copy2:
                    column_string = f"{item3:<22}"
                    for item4 in copy1:
                        try:
                            value = \
                                self._cross_table_statistics(item3, item4)[0]
                            column_string = \
                                f"{column_string} $  {value:<30.2f}"
                        except NoMatchingItems:
                            column_string = f"{column_string} {'$  N/A':<33}"
                    print(column_string)
            elif stat == stat.AVG:
                for item3 in copy2:
                    column_string = f"{item3:<22}"
                    for item4 in copy1:
                        try:
                            value = \
                                self._cross_table_statistics(item3, item4)[1]
                            column_string = \
                                f"{column_string} $  {value:<30.2f}"
                        except NoMatchingItems:
                            column_string = f"{column_string} {'$  N/A':<33}"
                    print(column_string)
            elif stat == stat.MAX:
                for item3 in copy2:
                    column_string = f"{item3:<22}"
                    for item4 in copy1:
                        try:
                            value = \
                                self._cross_table_statistics(item3, item4)[2]
                            column_string = \
                                f"{column_string} $  {value:<30.2f}"
                        except NoMatchingItems:
                            column_string = f"{column_string} {'$  N/A':<33}"
                    print(column_string)


""" This function in the program has a while loop, which prints the 
print_menu function, which presents the user with multiple options. 
Once the user has chosen an option, data will show up, or a message will 
show up asking the user to load the dataset first. Furthermore, the 
print_menu will be printed again so that the user can choose another 
option. Also, if the user enters letters or a number that is out of 
range, a clarifying statement will be printed so that the user enters 
only a valid number value, and then the print_menu will be printed once 
again. The new addition to the function is that it prints the 
currency_options function before the menu. More new additions is that 
the function prints the user's home currency first in the menu outside 
the loop, prints the header the user has chosen, and prints the 
copyright statement at the end outside the loop.
"""


def menu(dataset: DataSet):
    """ Allows the user to choose which option they want and either
    prints a data table or prints a message asking the user to load the
    dataset first, prints the header chosen, prints the menu, and
    prints the copyright statement.
    """
    currency_options(home_currency)
    while True:
        print(f"{dataset.header}")
        print_menu()
        users_choice = input("What is your choice? ")
        # Turns string answer into integer
        try:
            int_users_choice = int(users_choice)
        except ValueError:
            print("Please only enter a number value.")
            continue
        if int_users_choice == 1:
            try:
                dataset.display_cross_table(dataset.Stats.AVG)
            except EmptyDatasetError:
                print("Please Load A Dataset First")
        elif int_users_choice == 2:
            try:
                dataset.display_cross_table(dataset.Stats.MIN)
            except EmptyDatasetError:
                print("Please Load A Dataset First")
        elif int_users_choice == 3:
            try:
                dataset.display_cross_table(dataset.Stats.MAX)
            except EmptyDatasetError:
                print("Please Load A Dataset First")
        elif int_users_choice == 4:
            # dataset.load_default_data()
            dataset.load_file()
        elif int_users_choice == 9:
            print("Quitting now.")
            print("Goodbye.")
            break
        else:
            print("Please enter a valid number.")
    print(f"{DataSet.copyright}")


conversions = {'USD': 1, 'EUR': .84, 'CAD': 1.23, 'GBP': .72, 'CHF': .92,
               'NZD': 1.41, 'AUD': 1.32, 'JPY': 110.8}

"""
This function in the program contains a equation that converts 
currencies from one countries currency to another. Furthermore, there 
is an if statement that checks to see if the quantity is below zero, 
and if there is, then it will raise a ValueError.  
"""


def currency_converter(source_curr: str, target_curr: str, quantity: float) \
        -> float:
    """ Converts currencies from different countries.

    Args:
        source_curr (str): Represents the original currency
        target_curr (str): Represents the currency after exchange
        quantity (float): Represents the amount of money in original
            currency
    Returns:
        float: Converting between different currencies
    """
    if quantity < 0:
        raise ValueError("Quantity < 0")
    return conversions[target_curr] * quantity / conversions[source_curr]


""" This function in the program creates a table that converts from 
one currency to all the other currencies in the dictionary.
"""


def currency_options(base_curr: str):
    """ Converts one currency to multiple other currencies.

    Args:
        base_curr (str): Represents the currency chosen by the user
    """
    header_string = base_curr
    for item in conversions:
        if item == base_curr:
            continue
        else:
            header_string = f"{header_string:10}{item:10}"
    print(header_string)
    for number in range(10, 100, 10):
        conversion_string = f"{float(number):<10.2f}"
        for item in conversions:
            if item == base_curr:
                continue
            else:
                conversion_string = f"{conversion_string}" \
                        f"{currency_converter(base_curr, item, number):<10.2f}"
        print(conversion_string)


if __name__ == "__main__":
    main()


"""
--- sample run #1 ---
Please enter your name: Samarth
Hi Samarth, welcome to Foothill's database project.
Enter a header for the menu: AirBNB
What is your home currency? USD
Options for converting from USD:
USD       EUR       CAD       GBP       CHF       NZD       AUD       JPY       
10.00     8.40      12.30     7.20      9.20      14.10     13.20     1108.00   
20.00     16.80     24.60     14.40     18.40     28.20     26.40     2216.00   
30.00     25.20     36.90     21.60     27.60     42.30     39.60     3324.00   
40.00     33.60     49.20     28.80     36.80     56.40     52.80     4432.00   
50.00     42.00     61.50     36.00     46.00     70.50     66.00     5540.00   
60.00     50.40     73.80     43.20     55.20     84.60     79.20     6648.00   
70.00     58.80     86.10     50.40     64.40     98.70     92.40     7756.00   
80.00     67.20     98.40     57.60     73.60     112.80    105.60    8864.00   
90.00     75.60     110.70    64.80     82.80     126.90    118.80    9972.00   
AirBNB
Main Menu
1 - Print Average Rent by Location and Property Type
2 - Print Minimum Rent by Location and Property Type
3 - Print Maximum Rent by Location and Property Type
4 - Load Data
9 - Quit
What is your choice? 1
Please Load A Dataset First
AirBNB
Main Menu
1 - Print Average Rent by Location and Property Type
2 - Print Minimum Rent by Location and Property Type
3 - Print Maximum Rent by Location and Property Type
4 - Load Data
9 - Quit
What is your choice? 2
Please Load A Dataset First
AirBNB
Main Menu
1 - Print Average Rent by Location and Property Type
2 - Print Minimum Rent by Location and Property Type
3 - Print Maximum Rent by Location and Property Type
4 - Load Data
9 - Quit
What is your choice? 3
Please Load A Dataset First
AirBNB
Main Menu
1 - Print Average Rent by Location and Property Type
2 - Print Minimum Rent by Location and Property Type
3 - Print Maximum Rent by Location and Property Type
4 - Load Data
9 - Quit
What is your choice? 4
48895
AirBNB
Main Menu
1 - Print Average Rent by Location and Property Type
2 - Print Minimum Rent by Location and Property Type
3 - Print Maximum Rent by Location and Property Type
4 - Load Data
9 - Quit
What is your choice? 1
                       
AirBNB
Main Menu
1 - Print Average Rent by Location and Property Type
2 - Print Minimum Rent by Location and Property Type
3 - Print Maximum Rent by Location and Property Type
4 - Load Data
9 - Quit
What is your choice? 2
                       
AirBNB
Main Menu
1 - Print Average Rent by Location and Property Type
2 - Print Minimum Rent by Location and Property Type
3 - Print Maximum Rent by Location and Property Type
4 - Load Data
9 - Quit
What is your choice? 3
                       
AirBNB
Main Menu
1 - Print Average Rent by Location and Property Type
2 - Print Minimum Rent by Location and Property Type
3 - Print Maximum Rent by Location and Property Type
4 - Load Data
9 - Quit
What is your choice? 9
Quitting now.
Goodbye.
copyright 2021 Samarth Pasala
"""


# In[ ]:




