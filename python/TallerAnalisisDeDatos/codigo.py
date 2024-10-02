import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
from scipy import stats
from sklearn.preprocessing import LabelEncoder


tabla1= pd.read_excel('drive/MyDrive/Colab Notebooks/parte_1.xlsx', engine='openpyxl');
tabla2 = pd.read_excel('drive/MyDrive/Colab Notebooks/parte_2.xlsx', engine='openpyxl');
resultado = pd.merge(tabla1, tabla2, on="primary_key", how='left');
resultado.rename(columns={'primary_key': 'ID', 'Season': 'Temporada', 'U.S. viewers(millions)' : 'espectadores', 'Imdb rating' : 'rating'}, inplace=True)
pd.set_option('display.max_rows', None)
pd.set_option('display.max_columns', None)
columnas = ['espectadores', 'rating']

resultado = resultado.sort_values(by='ID')
fechaformato = '%Y-%m-%d'
resultado['Original air date'] = pd.to_datetime(resultado['Original air date'], format=fechaformato, errors='coerce')

def calcular_estadisticas(datos): 
  me = stats.mode(datos.dropna(), nan_policy='omit')
  moda = me[0] if not me.mode.size == 0 else None
  return {
      'Media': round(datos.mean(), 1),
        'Mediana': datos.median(),
        'Moda': moda,
        'Varianza': round(datos.var(), 1),
        'Desviación Estándar': round(datos.std(), 1),
        'Rango': datos.max() - datos.min()
        }       
def generar_tabla(resultado, columnas):
    return pd.DataFrame({col: calcular_estadisticas(resultado[col]) for col in columnas}).T

def rellenar_na_con_fecha_mas_cercana(df, columna_fecha):
    df[columna_fecha] = df[columna_fecha].fillna(method='ffill').fillna(method='bfill')
    return df

    tabla = generar_tabla(resultado, columnas)
print(tabla)

#Feemplazar los valores 
resultado['espectadores'].fillna(round(resultado['espectadores'].mean(), 1), inplace=True)
resultado['rating'].fillna(round(resultado['rating'].mean(), 1), inplace=True)

tablaActualizada = generar_tabla(resultado, columnas)
print(tablaActualizada)

print(resultado['Written by'])

moda_written_by = resultado['Written by'].mode()[0]  # mode() devuelve una Serie
resultado['Written by'].fillna(moda_written_by, inplace=True)
print(resultado['Written by'])


# Realizar One-Hot Encoding para la columna 'Written by'
resultado_encoded = pd.get_dummies(resultado, columns=['Written by'])
resultado_final = resultado_encoded[['ID'] + [col for col in resultado_encoded.columns if col.startswith('Written by_')]]
resultado_final_sorted = resultado_final.sort_values(by='ID')

resultado_final.to_excel('onehotencoding.xlsx', index=False)

le = LabelEncoder()
resultado['Directed by Encoded'] = le.fit_transform(resultado['Directed by'])
rf = resultado[['ID', 'Directed by Encoded']]
print(rf)


resultado = rellenar_na_con_fecha_mas_cercana(resultado, 'Original air date')
rx = resultado[['ID', 'Original air date']]

print(rx)

#Graficos
plt.figure(figsize=(10, 6))
plt.boxplot(resultado['rating'].dropna(), vert=False)
plt.title('Boxplot de Ratings')
plt.xlabel('Rating')
plt.show()


Q1 = resultado['rating'].quantile(0.25)
Q3 = resultado['rating'].quantile(0.75)

IQR = Q3 - Q1

lower_bound = Q1 - 1.5 * IQR
upper_bound = Q3 + 1.5 * IQR

outliers = resultado[(resultado['rating'] < lower_bound) | (resultado['rating'] > upper_bound)]
rx = outliers[['ID', 'rating']] 
print("Outliers:")
print(rx)



media_original = resultado['rating'].mean()
varianza_original = resultado['rating'].var()


Q1 = resultado['rating'].quantile(0.25)
Q3 = resultado['rating'].quantile(0.75)

IQR = Q3 - Q1

lower_bound = Q1 - 2 * IQR
upper_bound = Q3 + 2 * IQR

median_rating = resultado['rating'].median()
resultado['rating'] = resultado['rating'].apply(lambda x: median_rating if x < lower_bound or x > upper_bound else x)

media_ajustada = resultado['rating'].mean()
varianza_ajustada = resultado['rating'].var()

plt.figure(figsize=(10, 6))
sns.boxplot(x=resultado['rating'])
plt.title('Boxplot de Ratings (Con Outliers Reemplazados)')
plt.xlabel('Rating')
plt.show()

print("Media Original:", media_original)
print("Varianza Original:", varianza_original)
print("Media Ajustada:", media_ajustada)
print("Varianza Ajustada:", varianza_ajustada)



#graficos finales

# "Written by"
plt.figure(figsize=(8, 6))
resultado['Written by'].value_counts().plot(kind='pie', autopct='%1.1f%%')
plt.title('Distribución de "Written by"')
plt.ylabel('')
plt.show()

# Rating
plt.figure(figsize=(10, 6))
sns.lineplot(data=resultado.groupby('Temporada')['rating'].mean().reset_index(), x='Temporada', y='rating')
plt.title('Promedio de "Imdb rating" por Temporada')
plt.xlabel('Temporada')
plt.ylabel('Promedio de IMDB Rating')
plt.show()

# vistas
plt.figure(figsize=(10, 6))
sns.lineplot(data=resultado.groupby('Temporada')['espectadores'].mean().reset_index(), x='Temporada', y='espectadores')
plt.title('U.S. Viewers (millions) por Temporada')
plt.xlabel('Temporada')
plt.ylabel('U.S. Viewers (millions)')
plt.show()

media_rating_por_escritor = resultado.groupby('Written by')['rating'].mean()

media_rating_por_escritor = media_rating_por_escritor.sort_values()

plt.figure(figsize=(12, 6))
plt.plot(media_rating_por_escritor.index, media_rating_por_escritor.values, marker='o', linestyle='-')
plt.xlabel('Escritor')
plt.ylabel('Media de Calificación')
plt.title('Media de Calificación por Escritor')
plt.xticks(rotation=90)  
plt.grid(True)
plt.tight_layout()

plt.show()