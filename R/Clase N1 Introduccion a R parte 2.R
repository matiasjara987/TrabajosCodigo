#Matias Jara

#cargando datos en .xlsx
library(readxl)
PelicanStores <- read_excel("C:/Users/matia/Downloads/PelicanStores.xlsx", 
                            sheet = "Data")
attach(PelicanStores)
PelicanStores

summary(Net_Sales)



#para visualizar dos graficos debemos utilizar la funcion par
par(mfrow=c(1,1))
hist(Net_Sales)
boxplot(Net_Sales,horizontal = TRUE, col = "blue")
help("boxplot")


## boxplot on a formula:
boxplot(count ~ spray, data = InsectSprays, col = "lightgray")
# *add* notches (somewhat funny here <--> warning "notches .. outside hinges"):
boxplot(count ~ spray, data = InsectSprays,
        notch = TRUE, add = TRUE, col = "blue")


hist(Net_Sales, col="steelblue")
