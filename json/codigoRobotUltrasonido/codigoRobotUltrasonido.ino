#include <SoftwareSerial.h>
SoftwareSerial BTserial(2,3); // RX | TX

#define mL1 9
#define mL2 10
#define mR1 11
#define mR2 12
#define enL 5
#define enR 6
#define lnt 4  
int c=5;//Calibracion
//int v=0;
char dato;
//bool encendido=false;
int modo=0;


const int Trigger = 8;   // Trigger del sensor
const int Echo = 7;   //Echo del sensor


void setup()
{   
    Serial.begin(9600);
    /* Declaramos el pin 9 como salida del pulso ultrasonico */
    pinMode(9, OUTPUT);
    /* Declaramos el pin 8 como entrasa (tiempo que tarda en volver) */
    //pinMode(8, INPUT);
    /* Declaramos el pin 12 como el pin del LED */  
    pinMode(12, OUTPUT);


    pinMode(Trigger, OUTPUT); //pin como salida
    pinMode(Echo, INPUT);  //pin como entrada
    digitalWrite(Trigger, LOW);//Iniciar el pin con 0

  
    // HC-05 default serial speed for AT mode is 38400
    BTserial.begin(38400);   
    inicializar();    
    notificacion(3);
}

void loop()
{
  
    /* Se estabiliza el sensor */
    digitalWrite(Trigger, HIGH);
    delayMicroseconds(5);
    /* Se envia el pulso ultrasonico */
    digitalWrite(Trigger, LOW);
    delayMicroseconds(10);
    /* Mide el tiempo transcurrido entre la salida y la llegada del pulso ultrasonico */
    int t = pulseIn(Echo, HIGH);
    /* Se calcula la distancia on esta formila*/
    int d = t/59; 
    /* Si la distancia es menor a 15 centimetros prendemos el led */
    if (d <= 10 )
    {
      digitalWrite(12, HIGH);
      avanzar(0);

    }
    /* Si la distancia es mayor a 10 centimetros, el led permanece apagado */
    else
      digitalWrite(12, LOW);
 
    /* Se imprime la distancia en centimetros en el monitor serial */
    Serial.println("Distancia");
    Serial.println(d);



  
   dato=' ';
   if (BTserial.available())
       dato = BTserial.read();           
   if((int)dato!=32) 
      datoBlueTooth(dato);            
      /*
   if(modo==1){ remolino(0); }
   else if(modo==2){ serpiente(); }
   else if(modo==3){ cuadrado(); }*/
}

void datoBlueTooth(char d){
    notificacion(1);
    switch(d){
      /*case '0': velocidadM(255-(0*10), 255-(0*10)); v=0; break;
      case '1': velocidadM(255-(1*10), 255-(1*10)); v=1; break;
      case '2': velocidadM(255-(2*10), 255-(2*10)); v=2; break;
      case '3': velocidadM(255-(3*10), 255-(3*10)); v=3; break;
      case '4': velocidadM(255-(4*10), 255-(4*10)); v=4; break;
      case '5': velocidadM(255-(5*10), 255-(5*10)); v=5; break;
      case '6': velocidadM(255-(6*10), 255-(6*10)); v=6; break;
      case '7': velocidadM(255-(7*10), 255-(7*10)); v=7; break;
      case '8': velocidadM(255-(8*10), 255-(8*10)); v=8; break;
      case '9': velocidadM(255-(9*10), 255-(9*10)); v=9; break;  */
      case 'a':  avanzar(1);  break;//Adelante
      case 'b': avanzar(3);  break;//Izquierda
      case 'c': avanzar(4);  break;//Derecha
      case 'd': avanzar(2);  break;//Atras
      case 'f': avanzar(0);  break;//Parar
      /*case 'g': modo=1;  break;
      case 'h': modo=2;  break;
      case 'i': modo=3;  break  */
      default: break;
    }  
}


void notificacion(int n){
  
  for(int i=0;i<n;i++)
  {
    digitalWrite(lnt,LOW);
    delay(200);
    digitalWrite(lnt,HIGH);
    delay(200);    
  }
/*  if(encendido)  digitalWrite(13,HIGH);
  else digitalWrite(13,LOW);*/
    digitalWrite(lnt,LOW);
    
}

/*void cuadrado(){  
  avanzar(3);    
  delay(500);
  avanzar(1);
  delay(500);
  avanzar(4);  
  delay(500); 
  avanzar(1);
  delay(500);
}

void serpiente(){
  avanzar(3);    
  delay(500);
  avanzar(4);  
  delay(500); 
}
void remolino(int _v){
  avanzar(1);//Adelante
  velocidadM(255-((_v*10)+50),255-(_v*10));
}
*/


void velocidadM(int l, int r){
   analogWrite(enL,l);
   analogWrite(enR,r-c);
}

void avanzar(int i){
  
  switch(i){
    case 0://Apagar 
    digitalWrite (mL1, LOW);
    digitalWrite (mL2, LOW);
    digitalWrite (mR1, LOW);
    digitalWrite (mR2, LOW);  
    break;

    
    case 1://Adelante
    digitalWrite (mL1, HIGH);
    digitalWrite (mL2, LOW);
    digitalWrite (mR1, HIGH);
    digitalWrite (mR2, LOW);  
    
    break;
    case 2://Atras
    digitalWrite (mL1, LOW);
    digitalWrite (mL2, HIGH);
    digitalWrite (mR1, LOW);
    digitalWrite (mR2, HIGH); 
    break;
    case 3://Girar en su propio eje, izquierda
    digitalWrite (mL1, LOW);
    digitalWrite (mL2, HIGH);
    digitalWrite (mR1, HIGH);
    digitalWrite (mR2, LOW);  
    break;
    case 4://Girar en su propio eje, Derecha
    digitalWrite (mL1, HIGH);
    digitalWrite (mL2, LOW);
    digitalWrite (mR1, LOW);
    digitalWrite (mR2, HIGH); 
    break;
    default:break;
  }
  
}

void inicializar(){

  //Inicializa los pines para poder usarlos
  pinMode(lnt,OUTPUT);
  pinMode (mL1, OUTPUT);   
  pinMode (mL2, OUTPUT);   
  pinMode (mR1, OUTPUT);
  pinMode (mR2, OUTPUT);
  pinMode (mL2, OUTPUT);
  pinMode (enL, OUTPUT);
  pinMode (enR, OUTPUT);
  velocidadM(255, 255);
}