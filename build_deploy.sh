OUTPUT=$(ls -d */)

for i in ${OUTPUT}
do
    echo "$i"+"::::::::"
    mvn clean install -f $i/pom.xml
    echo "extracting dependencies"
    mkdir -p $i/target/dependency 
    (cd $i/target/dependency; jar -xf ../*.jar)
done
echo "building docker images"
docker-compose build --parallel
echo "docker compose build complete. Starting compose"
nohup docker-compose up &