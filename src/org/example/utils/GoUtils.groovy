package org.example.utils

class GoUtils implements Serializable {

    def steps  // Pipeline context

    GoUtils(steps) {
        this.steps = steps
    }

    void checkoutRepo(String repoUrl, String branch = 'main') {
        steps.stage('Checkout') {
            steps.echo "Cloning Employee API Repository..."
            steps.git branch: branch, url: repoUrl
        }
    }

    void installGoARM64() {
        steps.stage('Install Go (ARM64)') {
            steps.echo "Installing Go 1.22.1 for ARM64"
            steps.sh '''
                wget https://go.dev/dl/go1.22.1.linux-arm64.tar.gz
                sudo rm -rf /usr/local/go
                sudo tar -C /usr/local -xzf go1.22.1.linux-arm64.tar.gz
                export PATH=$PATH:/usr/local/go/bin
                go version
            '''
        }
    }

    void setupDependencies() {
        steps.stage('Setup Go Dependencies') {
            steps.echo "Installing Go Modules (mux, logrus)"
            steps.sh '''
                export PATH=$PATH:/usr/local/go/bin
                go mod tidy
                go get github.com/gorilla/mux
                go get github.com/sirupsen/logrus
                go list -m all
            '''
        }
    }

    void installGoSec() {
        steps.stage('Install GoSec (ARM / AArch64)') {
            steps.echo "Installing GoSec for ARM64 without Snap"
            steps.sh '''
                GSEC_VERSION=2.20.0
                wget https://github.com/securego/gosec/releases/download/v${GSEC_VERSION}/gosec_${GSEC_VERSION}_linux_arm64.tar.gz
                tar -xzf gosec_${GSEC_VERSION}_linux_arm64.tar.gz
                sudo mv gosec /usr/local/bin/gosec
                sudo chmod +x /usr/local/bin/gosec
                gosec --version
            '''
        }
    }

    void runGoSecScan() {
        steps.stage('Run GoSec Scan') {
            steps.echo "Running GoSec Scan on ARM64"
            steps.sh '''
                export PATH=$PATH:/usr/local/go/bin
                gosec ./... | tee gosec-report.txt
            '''
        }
    }

    void archiveReport() {
        steps.stage('Archive Report') {
            steps.echo "Archiving GoSec Scan Report"
            steps.archiveArtifacts artifacts: 'gosec-report.txt', fingerprint: true
        }
    }
}
