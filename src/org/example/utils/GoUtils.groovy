package org.example.utils

class GoUtils implements Serializable {

    static def installGoARM64() {
        println "Installing Go 1.22.1 for ARM64..."

        sh '''
            wget https://go.dev/dl/go1.22.1.linux-arm64.tar.gz
            sudo rm -rf /usr/local/go
            sudo tar -C /usr/local -xzf go1.22.1.linux-arm64.tar.gz
            export PATH=$PATH:/usr/local/go/bin
            go version
        '''
    }

    static def setupDependencies() {
        println "Installing Go Modules (gorilla/mux, logrus)..."

        sh '''
            export PATH=$PATH:/usr/local/go/bin
            go mod tidy
            go get github.com/gorilla/mux
            go get github.com/sirupsen/logrus
            go list -m all
        '''
    }

    static def installGoSecARM64() {
        println "Installing GoSec ARM64 without Snap..."

        sh '''
            GSEC_VERSION=2.20.0
            wget https://github.com/securego/gosec/releases/download/v${GSEC_VERSION}/gosec_${GSEC_VERSION}_linux_arm64.tar.gz
            
            tar -xzf gosec_${GSEC_VERSION}_linux_arm64.tar.gz
            sudo mv gosec /usr/local/bin/gosec

            gosec --version
        '''
    }

    static def runGoSecScan() {
        println "Running GoSec Scan..."

        sh '''
            export PATH=$PATH:/usr/local/go/bin
            gosec ./... | tee gosec-report.txt
        '''
    }
}
