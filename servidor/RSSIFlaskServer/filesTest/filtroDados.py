from kalman import KalmanFilter
import csv
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from sklearn.neighbors import KNeighborsClassifier
from sklearn.metrics import classification_report, confusion_matrix
from sklearn.preprocessing import StandardScaler
from sklearn.model_selection import train_test_split
from numpy import median

url = "valores.txt"
names = ['AP1', 'AP2', 'AP3', 'AP4', 'AP5', 'AP6', 'AP7', 'AP8', 'Class']
dataset = pd.read_csv(url, names=names)
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
print(confusion_matrix(y_test, y_pred))
print(classification_report(y_test, y_pred))

test = KalmanFilter(0.008, 5)
# Exemplo ruim
# plot_re_filter_ap('ap2','Centro-Sala')

# Exemplo bom
# plot_re_filter_ap('ap1','Canto-Sentido-Porta')

# Exemplo teste
# plot_re_filter_ap('ap3','Centro-Sala')

def filter_ap(data): 
    test = KalmanFilter(0.008,5)
    return [test.filter(x) for x in data]

def plot_signal_ap(data):
    filtered = filter_ap(data)
    print("Variância: " + str(np.var(filtered)))
#    print(median(filtered))
    maxx = str(median(filtered)+np.var(filtered))
    minn = str(median(filtered)-np.var(filtered))
    print("max: "+ maxx)
    print("min: "+ minn)
    plt.figure()
    plt.plot(data,'k.--',label='Sinal com ruído')
    plt.plot(filtered,'b-',label='Sinal filtrado por Kalman')
    plt.plot([median(filtered)]*len(filtered),'g-',label='Média do sinal filtrado')
    plt.legend()
    plt.xlabel('Iteração')
    plt.ylabel('Sinal RSSI')
    plt.show()


def new_location(name_location, data_location):
    X = data_location
    y = name_location   
    ap1 = [x['ap1'] for x in X]
    ap2 = [x['ap2'] for x in X]
    ap3 = [x['ap3'] for x in X]
    ap4 = [x['ap4'] for x in X]
    ap5 = [x['ap5'] for x in X]
    ap6 = [x['ap6'] for x in X]
    ap7 = [x['ap7'] for x in X]
    ap8 = [x['ap8'] for x in X]
    apf1 = filter_ap(ap1)
    apf2 = filter_ap(ap2)
    apf3 = filter_ap(ap3)
    apf4 = filter_ap(ap4)
    apf5 = filter_ap(ap5)
    apf6 = filter_ap(ap6)
    apf7 = filter_ap(ap7)
    apf8 = filter_ap(ap8)
    for a1,a2,a3,a4,a5,a6,a7,a8 in zip(apf1,apf2,apf3,apf4,apf5,apf6,apf7,apf8):
        write_filter2(a1,a2,a3,a4,a5,a6,a7,a8,y)


def write_filter(a1,a2,a3,a4,a5,a6,a7,a8,local):
    file = open("valoresFilter.txt","a+")
    file.write("{},{},{},{},{},{},{},{},{}\n".format(a1,a2,a3,a4,a5,a6,a7,a8,local))
    file.close()

def write_filter2(a1,a2,a3,a4,a5,a6,a7,a8,local):
    file = open("valoresNew.txt","a+")
    file.write("{},{},{},{},{},{},{},{},{}\n".format(a1,a2,a3,a4,a5,a6,a7,a8,local))
    file.close()

def localiza(rssi):
    dados = np.array(rssi)
    dados = dados.reshape(1, -1)
    dados = scaler.transform(dados)
    return classifier.predict(dados)[0]

def re_treino():
    dataset = pd.read_csv(url, names=names)
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
    print(confusion_matrix(y_test, y_pred))
    print(classification_report(y_test, y_pred))

def re_filter():
    dataset = pd.read_csv(url, names=names)
    X = dataset.iloc[:, :-1].values
    y = dataset.iloc[:, 8].values
    open("valoresFilter.txt","w").close()
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
        for a1,a2,a3,a4,a5,a6,a7,a8 in zip(apf1,apf2,apf3,apf4,apf5,apf6,apf7,apf8):
            write_filter(a1,a2,a3,a4,a5,a6,a7,a8,x)


def plot_re_filter_ap(ap_name,local):
    dataset = pd.read_csv(url, names=names)
    X = dataset.iloc[:, :-1].values
    y = dataset.iloc[:, 8].values
    locais = [x for x in y]
    locais = list(set(locais))
    # tam = len(locais)
    all = dataset.values
    if local in locais:
        aps = []
        a = [y for y in all if y[8] == local]
        ap1 = [x[0] for x in a]
        ap2 = [x[1] for x in a]
        ap3 = [x[2] for x in a]
        ap4 = [x[3] for x in a]
        ap5 = [x[4] for x in a]
        ap6 = [x[5] for x in a]
        ap7 = [x[6] for x in a]
        ap8 = [x[7] for x in a]
        result = {
            'ap1':ap1,
            'ap2':ap2,
            'ap3':ap3,
            'ap4':ap4,
            'ap5':ap5,
            'ap6':ap6,
            'ap7':ap7,
            'ap8':ap8,
        }.get(ap_name,[])
        if result != []:return plot_signal_ap(result)
        else: print('Especifique o ap: ap1,ap2,ap3...ap8')
    else:
        print ('local não encontrado nos dados, locais disponíveis:')
        for x in locais: print (x)

def less_error():
    error = []
    for i in range(1, 100):  
        knn = KNeighborsClassifier(n_neighbors=i)
        knn.fit(X_train, y_train)
        pred_i = knn.predict(X_test)
        error.append(np.mean(pred_i != y_test))
    plt.figure(figsize=(12, 6))  
    plt.plot(range(1, 100), error, color='red', linestyle='dashed', marker='o', markerfacecolor='blue', markersize=10)
    plt.title('Error Rate K Value')  
    plt.xlabel('K Value')  
    plt.ylabel('Mean Error')  
    plt.show()
