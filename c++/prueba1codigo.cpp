int luces[] = {13, 12, 11,10,9,9};
int n = 6;

void setup()
{
ciclo();
}

void loop()
{
  recorrerGradual();
}


void ciclo()
{
 for(int i = 0; i < n; i++) {
    pinMode(luces[i], OUTPUT);
  }
}

void encenderGradual(int luz){
  int d = 10000 / (255/25);  
  for(int a = 0; a <= 255; a += 25){
    analogWrite(luz, a);
    delay(d); 
  }
	analogWrite(luz, 0);  
}

void recorrerGradual(){
 for(int i = 0; i < n; i++) {
    encenderGradual(luces[i]); 
  }
}
