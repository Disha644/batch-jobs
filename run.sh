CURRENT_DATE=`date '+%Y/%m/%d'`
mvn clean package -Dmaven.test.skip=true;
java -jar ./target/linkedin-batch-*-*-0.0.2.jar "item=tops" "run.date(date)=$CURRENT_DATE" "address=Agra" "type=sunflower";
read;
