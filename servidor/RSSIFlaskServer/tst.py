from kalman2 import KalmanFilter
import matplotlib.pyplot as plt

test = KalmanFilter(0.008, 0.1)
x = [66,64,63,63,63,66,65,67,58,10,10,10,10]
plt.rcParams['figure.figsize'] = (10, 8)

def filter_ap(data): 
    return [test.filter(x) for x in data]

def plot_signal_ap(data):
    filtered = filter_ap(data)
    plt.figure()
    plt.plot(data,'k+',label='Sinal com ruído')
    plt.plot(filtered,'b-',label='Sinal filtrado por kalman')
    plt.legend()
    plt.title('Filtro de kalman', fontweight='bold')
    plt.xlabel('Iteração')
    plt.ylabel('Sinal RSSI')
    plt.show()


