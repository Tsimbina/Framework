#!/bin/bash

APP_NAME="Framework-mvc-t"
SRC_DIR="src"
BUILD_DIR="build"
LIB_DIR="lib"

rm -rf $BUILD_DIR
mkdir -p $BUILD_DIR/

# Construction du classpath avec tous les jars
CLASSPATH=$(echo $LIB_DIR/*.jar | tr ' ' ':')

echo "Classpath utilisé :"
echo $CLASSPATH

# Liste des sources
find $SRC_DIR -name "*.java" > sources.txt

# Compilation
javac -cp "$CLASSPATH" \
      -d $BUILD_DIR/ \
      @sources.txt

if [ $? -ne 0 ]; then
    echo "Erreur compilation"
    exit 1
fi

rm sources.txt

# Copie des fichiers WEB-INF si nécessaire
# cp -r web.xml build/WEB-INF/

# Création du jar
jar -cvf $APP_NAME.jar -C $BUILD_DIR .

echo ""
echo "Compilation terminée."
echo "Déploiement terminé. Redémarrez Tomcat si nécessaire."