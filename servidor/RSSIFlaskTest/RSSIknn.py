import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
from sklearn.neighbors import KNeighborsClassifier
from sklearn.metrics import classification_report, confusion_matrix
from sklearn.preprocessing import StandardScaler
from sklearn.model_selection import train_test_split

#url = "valoresNew.txt"
url = "valores.txt"
names = ['AP1', 'AP2', 'AP3', 'AP4', 'AP5', 'AP6', 'AP7', 'AP8', 'Class']
#names = ['AP1', 'AP2', 'Class']
# Read dataset to pandas dataframe
dataset = pd.read_csv(url, names=names)
X = dataset.iloc[:, :-1].values
y = dataset.iloc[:, 8].values
#y = dataset.iloc[:, 2].values
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.20)
scaler = StandardScaler()  
scaler.fit(X_train)
X_train = scaler.transform(X_train)  
X_test = scaler.transform(X_test)
classifier = KNeighborsClassifier(n_neighbors=10)  
classifier.fit(X_train, y_train)
y_pred = classifier.predict(X_test)
# print(confusion_matrix(y_test, y_pred))
# print(classification_report(y_test, y_pred))


def compara_treino():
    url_treino = "dados/valores50Filter.txt"
    url_teste = "dados/valoresMais3.txt" 
    names = ['AP1', 'AP2', 'AP3', 'AP4', 'AP5', 'AP6', 'AP7', 'AP8', 'Class']
    dataset_treino = pd.read_csv(url_treino, names=names)
    dataset_teste = pd.read_csv(url_teste, names=names)
    X_treino = dataset_treino.iloc[:, :-1].values
    y_treino = dataset_treino.iloc[:, 8].values
    X_teste = dataset_teste.iloc[:, :-1].values
    y_teste = dataset_teste.iloc[:, 8].values
    scaler = StandardScaler()  
    scaler.fit(X_treino)

    X_treino = scaler.transform(X_treino)  
    X_teste = scaler.transform(X_teste)
    classifier = KNeighborsClassifier(n_neighbors=5)  
    classifier.fit(X_treino, y_treino)
    y_pred = classifier.predict(X_teste)
    i = 0
    for x,y in zip(y_teste,y_pred):
        if x == y:
            i+=1 
        print("real:{}, pred:{}".format(x,y))
    print("Taxa de acerto: ", i/len(y_teste) *100,"%")


compara_treino()


def localiza(rssi):
    dados = np.array(rssi)
    dados = dados.reshape(1, -1)
    dados = scaler.transform(dados)
    return classifier.predict(dados)[0]

def re_treino():
    X = dataset.iloc[:, :-1].values
    #y = dataset.iloc[:, 8].values
    y = dataset.iloc[:, 2].values
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.20)
    scaler = StandardScaler()  
    scaler.fit(X_train)
    X_train = scaler.transform(X_train)  
    X_test = scaler.transform(X_test)
    classifier = KNeighborsClassifier(n_neighbors=5)  
    classifier.fit(X_train, y_train)


def less_error():
    error = []
    for i in range(1, 40):  
        knn = KNeighborsClassifier(n_neighbors=i)
        knn.fit(X_train, y_train)
        pred_i = knn.predict(X_test)
        error.append(np.mean(pred_i != y_test))
    plt.figure(figsize=(12, 6))  
    plt.plot(range(1, 40), error, color='red', linestyle='dashed', marker='o', markerfacecolor='blue', markersize=10)
    plt.title('Error Rate K Value')  
    plt.xlabel('K Value')  
    plt.ylabel('Mean Error')  
    plt.show()


#less_error()

    

