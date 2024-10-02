int calleRojo = 12;
int calleAmarillo = 11;
int calleVerde = 10;

int PeatonRojo = 9;
int PeatonVerde = 8;

int calleDosRojo = 7;
int calleDosAmarillo = 6;
int calleDosVerde = 5;

int PeatonDosRojo = 4;
int PeatonDosVerde= 3;

void setup(){
  pinMode(calleRojo, OUTPUT);
  pinMode(calleAmarillo, OUTPUT);
  pinMode(calleVerde, OUTPUT);

  pinMode(PeatonRojo, OUTPUT);
  pinMode(PeatonVerde, OUTPUT);
  pinMode(calleDosRojo, OUTPUT);
  pinMode(calleDosAmarillo, OUTPUT);
  pinMode(calleDosRojo, OUTPUT);
  pinMode(PeatonDosRojo, OUTPUT);
  pinMode(PeatonDosVerde, OUTPUT);

}

void loop(){
    digitalWrite(calleVerde, HIGH);
    digitalWrite(PeatonDosVerde, HIGH);
    digitalWrite(calleDosRojo, HIGH);
    digitalWrite(calleDosRojo, HIGH);
    delay(3000);

    digitalWrite(calleVerde, LOW);
    digitalWrite(calleAmarillo, HIGH);
    delay(1000);

    digitalWrite(calleAmarillo, LOW);
    digitalWrite(PeatonDosVerde, LOW);
    digitalWrite(calleDosRojo, LOW);
    digitalWrite(calleDosRojo, LOW);

  digitalWrite(calleRojo,      HIGH);
  digitalWrite(PeatonVerde,     HIGH);
  digitalWrite(calleDosVerde,     HIGH);
  digitalWrite(PeatonDosRojo,      HIGH);
  delay(3000);                             
