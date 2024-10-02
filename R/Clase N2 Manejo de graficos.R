#cargando datos en .xlsx
library(readxl)
PelicanStores <- read_excel("C:/Users/matia/Downloads/PelicanStores.xlsx", 
                            sheet = "Data")
attach(PelicanStores)

#para visualizar dos graficos debemos utilizar la funcion par
par(mfrow=c(2,1))

hist(
     Net_Sales,
     col = "#e75b3d", 
     main="Histograma de Venta Neta",
     xlab= "Venta neta (dólares)",
     ylab= "Frecuencia"
     )
     
boxplot(
  Net_Sales,
  horizontal = TRUE, 
  main = "Venta neta",
  col = "steelblue"
  )

 #¿Podemos ver las ventas de los tipos de clientes por separado?
  table(Type_of_Customer)
  summary(Net_Sales)
  tapply(Net_Sales, Type_of_Customer, summary)
  #Primer argumento: es la variable 
  #Segundo argumento: Sería una cualidad por la que se filtra
  #Tercer Argumento: Es la funcion que utilizamos, en este caso summary 
  
  
help("boxplot")
#Para poder definir colores lo podemos hacer colocando el nommbre 
 # col = "nombre de color"
 # rgb = (1,0,1, 0.5) donde la posición 1 es rojo, el siguiente es verde y el último es azul y el 0.5 representa el degradado
 # hex = #e75b3d