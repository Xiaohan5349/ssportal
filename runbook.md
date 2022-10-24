# Guide Book of MFA Portal


## Prerequisite

- #### Angular
- #### Java 11

## Build MFA Package

```
#Clone the whole project to device, choose branch based on the env
git clone MFA_Git_Repo_Url_ (only needed at first time)

# Go to FE directory
cd Path_to_MfaCode/fe

# Update code and install needed modules
git pull
npm install (only needed at first time)

# Build Frontend
ng build --prod --base-href ./

# After the build completed, move all files under directory Path_to_MfaCode/fe/dist/fe/ to Path_to_MfaCode/be/src/main/resources/static
cd Path_to_MfaCode/be/src/main/resources/static
rm -rf *
mv Path_to_MfaCode/fe/dist/fe/* .

# Build Backend 
cd Path_to_MfaCode/be
mvn clean
mvn package

# You can find war file under directory Path_to_MfaCode/be/target, just upload the war file to the server you gonna to deploy mfa service. To deploy the war file, first stop the mfa service on server.
sudo systemctl stop mfa
cd /opt/iam/mfa/webapps
mv Path_to_mfa.war sso.war
sudo systemctl start mfa

# to track mfa service running status
ps -ef|grep tomcat

# track logs of mfa
tail -f /opt/iam/mfa/logs/catalina.out


```