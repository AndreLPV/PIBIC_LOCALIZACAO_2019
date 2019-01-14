from kalman2 import KalmanFilter
import numpy as np
import pandas as pd
import csv
import matplotlib.pyplot as plt

def filter_ap(data): 
    return [test.filter(x) for x in data]

def plot_signal_ap(data):
    filtered = filter_ap(data)
    plt.figure()
    plt.plot(data,'k.--',label='Sinal com ruído')
    plt.plot(filtered,'b-',label='Sinal filtrado por kalman')
    plt.legend()
    plt.title('Filtro de kalman', fontweight='bold')
    plt.xlabel('Iteração')
    plt.ylabel('Sinal RSSI')
    plt.show()

def write_filter(a1,a2,a3,a4,a5,a6,a7,a8,local):
    file = open("valoresFilter.txt","a+")
    file.write("{},{},{},{},{},{},{},{},{}\n".format(a1,a2,a3,a4,a5,a6,a7,a8,local))
    file.close() 
    

test = KalmanFilter(0.008, 0.1)




url = "valores2.txt"

names = ['AP1', 'AP2', 'AP3', 'AP4', 'AP5', 'AP6', 'AP7', 'AP8', 'Class']

dataset = pd.read_csv(url, names=names)
X = dataset.iloc[:, :-1].values
y = dataset.iloc[:, 8].values

locais = [x for x in y]
locais = list(set(locais))
# tam = len(locais)
all = dataset.values
for x in locais:
    aps = []
    a = [y for y in all if y[8] == x]
    ap1 = [x[0] for x in a]
    ap2 = [x[1] for x in a]
    ap3 = [x[2] for x in a]
    ap4 = [x[3] for x in a]
    ap5 = [x[4] for x in a]
    ap6 = [x[5] for x in a]
    ap7 = [x[6] for x in a]
    ap8 = [x[7] for x in a]
    apf1 = filter_ap(ap1)
    apf2 = filter_ap(ap2)
    apf3 = filter_ap(ap3)
    apf4 = filter_ap(ap4)
    apf5 = filter_ap(ap5)
    apf6 = filter_ap(ap6)
    apf7 = filter_ap(ap7)
    apf8 = filter_ap(ap8)
#    for a1,a2,a3,a4,a5,a6,a7,a8 in zip(apf1,apf2,apf3,apf4,apf5,apf6,apf7,apf8):
#        write_filter(a1,a2,a3,a4,a5,a6,a7,a8,x)
    print(x)

plot_signal_ap(ap1)


    
    



def localiza(rssi):
    dados = np.array(rssi)
    dados = dados.reshape(1, -1)
    dados = scaler.transform(dados)
    return classifier.predict(dados)[0]

def re_treino():
    X = dataset.iloc[:, :-1].values
    y = dataset.iloc[:, 8].values
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.20)
    scaler = StandardScaler()  
    scaler.fit(X_train)
    X_train = scaler.transform(X_train)  
    X_test = scaler.transform(X_test)
    classifier = KNeighborsClassifier(n_neighbors=5)  
    classifier.fit(X_train, y_train)
    y_pred = classifier.predict(X_test)

    
