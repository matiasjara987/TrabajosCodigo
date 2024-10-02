#cargando datos en .xlsx
library(readxl)
PelicanStores <- read_excel("C:/Users/matia/Downloads/PelicanStores.xlsx", 
                            sheet = "Data")
attach(PelicanStores)

help("tapply")

#table sirve para filtrar los datos pro el campo
table(Gender)

# ¿Hay otra función además summary para resumir datos? R. Si, tenemos skimr pero es un paquete externo
#install.packages("skimr")  # - paquete para análisis descriptivo
library(skimr)
skim(Net_Sales)
# install.packages("beanplot") # - paquete para gráfico beanplot 
library(beanplot)


tapply(Age, Marital_Status, skim)
hist(Items)
  

# 1
tapply(Net_Sales, Type_of_Customer, summary)

#2
tapply(Net_Sales, Method_of_Payment, summary)

#3 
tapply(Net_Sales, Marital_Status , summary)


 #Items

#4
tapply(Items,Type_of_Customer , summary)


#5
tapply(Items, Method_of_Payment, summary)

#6 
tapply(Items, Marital_Status, summary)

 
 #Age
 
#7
tapply(Age, Type_of_Customer, summary)


#8 
tapply(Age, Method_of_Payment, summary)

#9
tapply(Age, Marital_Status, summary)


#Graficos
par(mfrow=c(3,2))
hist(Items)

boxplot(
  Items,
  col="steelblue",
  main = "Boxplot para items",
  horizontal = TRUE
  )

hist(Age)

boxplot(
  Age,
  col="orange",
  main = "Boxplot para Age",
  horizontal = TRUE
  )

  #Vector encargado de dar colores para mis datos
  b <- c("green", "red", "orange", "white", "blue")
hist(Net_Sales)
par(mfrow= c(2,1))
boxplot(
  Net_Sales~Method_of_Payment,
  cex.axis= 2,
  col=b,
  main = "Boxplot para Net_sales",
  horizontal = FALSE
    )
beanplot(
  Net_Sales~Method_of_Payment,
  col=b,
  main = "Boxplot para Net_sales",
  horizontal = FALSE
)


#install.packages("ggplot2") # - librería para gráficos sofisticados
#install.packages("esquisse")
library(ggplot2)
library(esquisse)
#Herramientas basadas en ggplot2
help("ggplot2")
esquisse::esquisser()
#para poder hacer un grafico con ggplot utilizamos squisse y luego copiamos el código y podemos ver en 
#el siguiente bloque 

#Código creado mediante esquisse:esquisser()
ggplot(PelicanStores) +
  aes(x = Net_Sales, fill = Type_of_Customer) +
  geom_histogram(bins = 10L) +
  scale_fill_hue(direction = 1) +
  labs(
    x = "Venta Neta(dólares)",
    y = "Frecuencia",
    title = "Histograma",
    subtitle = "histograma Venta Neta"
  ) +
  theme_minimal()

