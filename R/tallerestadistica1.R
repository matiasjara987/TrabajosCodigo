  #taller estadistica
  View(parte_1)
  names(parte_1)

    unida <-merge(
        x = parte_1,
        y = parte_2,
        by = "primary_key"
        )
  attach(unida)
   
  ## Librerias que voy a cargar para este proyecto
  library(esquisse)
  library(skimr)
  library(ggplot2)
  
  
   #Funciones
  moda <- function(columna) {
      return(as.numeric(names(which.max(table(columna)))))
  }
  media <- function(columna) {
    return(mean(columna, trim = 0, na.rm = TRUE))
  }
  mediana <- function(i) {
    return(median(i, na.rm = TRUE))
  }
  varianza <- function(dato) {
    return(var(dato, y= NULL, na.rm = TRUE))
  }
  desviacion <- function(dato){
    return(sd(dato, na.rm= TRUE))
  }
  rango <- function(dato){
    return(max(dato, na.rm = TRUE) - min(dato, na.rm = TRUE))
  }
  tabla <- function(datos){
    a <-media(datos)
    b <-mediana(datos)
    c <-moda(datos)
    d <-varianza(datos)
    e <- desviacion(datos)
    f <- rango(datos)
    df <-data.frame(
        Estadistica = c("Media", "Mediana", "Moda", "Varianza", "Desviación", "Rango"),
        Valor = c(a, b, c, d, e, f))
    return(df)
  }
  View(tabla(unida$`Imdb rating`))  
  
  
   #4
  # install.packages("dplyr")
  library(dplyr)
  x <- mediana(unida$`Imdb rating`)
  unida$`Imdb rating`[is.na(unida$`Imdb rating`)] <- x
  
  y <- mediana(unida$`U.S. viewers(millions)`)  
  unida$`U.S. viewers(millions)`[is.na(unida$`U.S. viewers(millions)`)] <- y  
View(unida)

 
modaEscritor <- "David Benioff & D. B. Weiss"
  unida$`Written by`[is.na(unida$`Written by`)]  <- modaEscritor
  
  install.packages("conflicted")
  
  #5
  #df es el dataframe de pruebas, el dforiginal es el dataframe original NO TRABAJAR CON EL NI MODIFICAR
  
  columna_dummy <- function(df, columna) {
    df %>% 
      mutate_at(columna, ~paste(columna, eval(as.symbol(columna)), sep = "_")) %>% 
      mutate(valor = 1) %>% 
      spread(key = columna, value = valor, fill = 0)
  }
  columna_dummy(df, Written.by)

  #Duplico la columna directed by para no perder informacion  
  df <- df %>%
    mutate('Dirigido por'  = `Directed by`)
  #Hago el label enconding con base r
  df$`Directed by` <- as.numeric(factor(df$`Directed by`))
  
  View(df)
  
  #6
  #df copia para probar cosas luego se borra
  