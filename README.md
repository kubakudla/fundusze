README
====

1) uruchom aplikację:

mvn package

java -jar target/fundusze.jar

4) Dostęp z poziomu swaggera:

http://localhost:8080/test/swagger-ui.html 


Przykladowe zapytanie:

{
  "fundusze": [
   {
      "id": 1,
      "nazwa": "Fundusz Polski 1",
      "rodzaj": "POLSKI"
    },
 {
      "id": 2,
      "nazwa": "Fundusz Zagraniczny 1",
      "rodzaj": "ZAGRANICZNY"
    },
 {
      "id": 3,
      "nazwa": "Fundusz Pieniężny 1",
      "rodzaj": "PIENIEZNY"
    }
  ],
  "kwota": 100,
  "stylInwestowania": "BEZPIECZNY"
}
