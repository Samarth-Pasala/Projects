import random

#Welcome Statement
print("Welcome to the random password generator!")
print()

#Empty list which will be appended to when the user selects categories 
password_components = []

#Lists of all the available categories with five elements in each list
sports = ['Basketball', 'Football', 'Baseball', 'Soccer', 'Hockey']
colors = ['Blue', 'green', 'Orange', 'pink', 'Yellow']
animals = ['Dog', 'cat', 'Monkey', 'giraffe', 'Elephant']
food = ['Cookies', 'noodles', 'Chicken', 'shrimp', 'Fish']
punctuation = ['$', '.', '!', '#', '@']
numbers = ['1', '2', '3', '4', '5']

#Main input statement
user_input_1 = input("How secure would you like your password to be? ")
print()

#Function that takes the user input as an argument and runs a section of code based on it
def sequence(user_input):

  #What is run if the argument passed is "secure"
  if user_input == "secure":
    category1 = list(input("Please choose two of the following categories: sports, colors, animals, food - ").split(", "))

    #Loops through the user selections and grabs a random element from the lists and appends it into the password_components list
    
    for components in category1:
      if components == "sports":
        component1 = random.choice(sports)
        password_components.append(component1)
      elif components == "colors":
        component1 = random.choice(colors)
        password_components.append(component1)
      elif components == "animals":
        component1 = random.choice(animals)
        password_components.append(component1)
      elif components == "food":
        component1 = random.choice(food)
        password_components.append(component1)
    print()
    print("You randomly generated password is:")
    print("".join(password_components))

  #What is run if the argument passed is "very secure"
  if user_input == "very secure":
    category1 = list(input("Please choose three of the following categories: sports, colors, animals, food - ").split(", "))
    
    #Loops through the user selections and grabs a random element from the lists and appends it into the password_components list
    
    for components in category1:
      if components == "sports":
        component1 = random.choice(sports)
        password_components.append(component1)
      elif components == "colors":
        component1 = random.choice(colors)
        password_components.append(component1)
      elif components == "animals":
        component1 = random.choice(animals)
        password_components.append(component1)
      elif components == "food":
        component1 = random.choice(food)
        password_components.append(component1)
      
    #Unique Aspect to the "very secure" password
    category2 = list(input("Please choose one of the following categories: punctuation, numbers - ").split())
    for components in category2:
      if components == "punctuation":
        component1 = random.choice(punctuation)
        password_components.append(component1)
      elif components == "numbers":
        component1 = random.choice(numbers)
        password_components.append(component1)
    print()
    print("Your randomly generated password is:")

    #joins all elements together to generate password
    print("".join(password_components)) 

#call to function
sequence(user_input_1) 
