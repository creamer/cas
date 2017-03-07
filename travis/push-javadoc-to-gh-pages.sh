#!/bin/bash
<<<<<<< HEAD
#
# Licensed to Apereo under one or more contributor license
# agreements. See the NOTICE file distributed with this work
# for additional information regarding copyright ownership.
# Apereo licenses this file to you under the Apache License,
# Version 2.0 (the "License"); you may not use this file
# except in compliance with the License.  You may obtain a
# copy of the License at the following location:
#
#   http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
#

invokeJavadoc=false
invokeDoc=false
=======
invokeJavadoc=false
invokeDoc=false
branchVersion="development"
>>>>>>> 628ba803f8075af6c61ffa1757851402b850ad18

# Only invoke the javadoc deployment process
# for the first job in the build matrix, so as
# to avoid multiple deployments.

if [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "master" ]; then
  case "${TRAVIS_JOB_NUMBER}" in
<<<<<<< HEAD
       *\.1) 
        echo -e "Invoking auto-doc deployment for Travis job ${TRAVIS_JOB_NUMBER}"
        invokeJavadoc=true;
=======
       *\.1)
        echo -e "Invoking auto-doc deployment for Travis job ${TRAVIS_JOB_NUMBER}"
        # Do not deploy javadocs to gh-pages as they are pulled from Maven Central
        # invokeJavadoc=true;
>>>>>>> 628ba803f8075af6c61ffa1757851402b850ad18
        invokeDoc=true;;
  esac
fi

echo -e "Starting with project documentation...\n"

if [ "$invokeDoc" == true ]; then

  echo -e "Copying project documentation over to $HOME/docs-latest...\n"
<<<<<<< HEAD
  cp -R cas-server-documentation $HOME/docs-latest
=======
  cp -R docs/cas-server-documentation $HOME/docs-latest
>>>>>>> 628ba803f8075af6c61ffa1757851402b850ad18

fi

echo -e "Finished with project documentation...\n"

echo -e "Staring with project Javadocs...\n"

if [ "$invokeJavadoc" == true ]; then

  echo -e "Started to publish latest Javadoc to gh-pages...\n"

<<<<<<< HEAD
  echo -e "Invoking Maven to generate the project site...\n"
  mvn -T 20 site site:stage -q -ff -B -P nocheck -Dversions.skip=false
  
  echo -e "Copying the generated docs over...\n"
  cp -R target/staging $HOME/javadoc-latest
=======
  echo -e "Invoking build to generate the project site...\n"
  ./gradlew javadoc -q -Dorg.gradle.configureondemand=true -Dorg.gradle.workers.max=8 --parallel

  echo -e "Copying the generated docs over...\n"
  cp -R build/javadoc $HOME/javadoc-latest
>>>>>>> 628ba803f8075af6c61ffa1757851402b850ad18

fi

echo -e "Finished with project Javadocs...\n"

if [[ "$invokeJavadoc" == true || "$invokeDoc" == true ]]; then

  cd $HOME
  git config --global user.email "travis@travis-ci.org"
  git config --global user.name "travis-ci"
<<<<<<< HEAD
  echo -e "Cloning the gh-pages branch...\n"
  git clone --quiet --branch=gh-pages https://${GH_TOKEN}@github.com/Jasig/cas gh-pages > /dev/null

  cd gh-pages

  echo -e "Staring to move project documentation over...\n"

  if [ "$invokeDoc" == true ]; then
    echo -e "Removing previous documentation from development...\n"
    git rm -rf ./development > /dev/null

    echo -e "Creating development directory...\n"
    test -d "./development" || mkdir -m777 -v ./development

    echo -e "Copying new docs from $HOME/docs-latest over to development...\n"
    cp -Rf $HOME/docs-latest/* ./development
    echo -e "Copied project documentation...\n"
  fi

  echo -e "Staring to move project Javadocs over...\n"

  if [ "$invokeJavadoc" == true ]; then
    echo -e "Removing previous Javadocs from /development/javadocs...\n"
    git rm -rf ./development/javadocs > /dev/null

    echo -e "Creating development directory...\n"
    test -d "./development" || mkdir -m777 -v ./development

    echo -e "Creating Javadocs directory at /development/javadocs...\n"
    test -d "./development/javadocs" || mkdir -m777 -v ./development/javadocs

    echo -e "Copying new Javadocs...\n"
    cp -Rf $HOME/javadoc-latest/* ./development/javadocs
=======
  git config --global pack.threads "24"
  
  echo -e "Cloning the repository to push documentation...\n"
  git clone --single-branch --depth 3 --branch gh-pages --quiet https://${GH_TOKEN}@github.com/apereo/cas gh-pages > /dev/null
  
  cd gh-pages
  git gc --aggressive --prune=now
  
  echo -e "Configuring tracking branches for repository:\n"
  for branch in `git branch -a | grep remotes | grep -v HEAD | grep -v master`; do
     git branch --track ${branch##*/} $branch
  done

  echo -e "Switching to gh-pages branch\n"
  git checkout gh-pages
  git status

  echo -e "\nStaring to move project documentation over...\n"

  if [ "$invokeDoc" == true ]; then
    echo -e "Removing previous documentation from $branchVersion...\n"
    git rm -rf ./"$branchVersion" > /dev/null

    echo -e "Creating $branchVersion directory...\n"
    test -d "./$branchVersion" || mkdir -m777 -v "./$branchVersion"

    echo -e "Copying new docs from $HOME/docs-latest over to $branchVersion...\n"
    cp -Rf $HOME/docs-latest/* "./$branchVersion"
    echo -e "Copied project documentation...\n"
  fi

  if [ "$invokeJavadoc" == true ]; then
    echo -e "Staring to move project Javadocs over...\n"

    echo -e "Removing previous Javadocs from /$branchVersion/javadocs...\n"
    git rm -rf ./"$branchVersion"/javadocs > /dev/null

    echo -e "Creating $branchVersion directory...\n"
    test -d "./$branchVersion" || mkdir -m777 -v ./"$branchVersion"

    echo -e "Creating Javadocs directory at /$branchVersion/javadocs...\n"
    test -d "./$branchVersion/javadocs" || mkdir -m777 -v ./"$branchVersion"/javadocs

    echo -e "Copying new Javadocs...\n"
    cp -Rf $HOME/javadoc-latest/* ./"$branchVersion"/javadocs
>>>>>>> 628ba803f8075af6c61ffa1757851402b850ad18
    echo -e "Copied project Javadocs...\n"

  fi

  echo -e "Adding changes to the git index...\n"
  git add -f . > /dev/null

  echo -e "Committing changes...\n"
<<<<<<< HEAD
  git commit -m "Published documentation to [gh-pages]. Build $TRAVIS_BUILD_NUMBER" > /dev/null

  echo -e "Pushing upstream to origin...\n"
  git push -fq origin gh-pages > /dev/null

  echo -e "Successfully published documenetation to [gh-pages] branch.\n"
=======
  git commit -m "Published documentation from $TRAVIS_BRANCH to [gh-pages]. Build $TRAVIS_BUILD_NUMBER " > /dev/null
  
  echo -e "Before: Calculating git repository disk usage:\n"
  du -sh .git/

  echo -e "Before: Counting git objects in the repository:\n"
  git count-objects -vH
  
  echo -e "\nCleaning up repository...\n"
  # rm -rf .git/refs/original/
  # rm -Rf .git/logs/
  # git reflog expire --expire=now --all
  
  echo -e "\nRunning garbage collection on the git repository...\n"
  git gc --prune=now
  git repack -a -d --depth=500000 --window=500
  
  echo -e "After: Calculating git repository disk usage:\n"
  du -sh .git/
  
  echo -e "After: Counting git objects in the repository:\n"
  git count-objects -vH
  
  echo -e "Pushing upstream to origin/gh-pages...\n"
  git push -fq origin --all > /dev/null

  echo -e "Successfully published documentation.\n"
>>>>>>> 628ba803f8075af6c61ffa1757851402b850ad18

fi
