def call() {
    node {
        stage('Checkout') {
            checkoutSCM()
        }

        stage('Install Go (ARM64)') {
            org.example.utils.GoUtils.installGoARM64()
        }

        stage('Setup Go Dependencies') {
            org.example.utils.GoUtils.setupDependencies()
        }

        stage('Install GoSec (ARM64)') {
            org.example.utils.GoUtils.installGoSecARM64()
        }

        stage('Run GoSec Scan') {
            org.example.utils.GoUtils.runGoSecScan()
        }

        stage('Archive Report') {
            archiveArtifacts artifacts: 'gosec-report.txt', fingerprint: true
        }
    }
}

def checkoutSCM() {
    echo "Cloning Employee API Repository..."
    checkout([
        $class: 'GitSCM',
        branches: [[name: "*/main"]],
        userRemoteConfigs: [[url: "https://github.com/OT-MICROSERVICES/employee-api.git"]]
    ])
}
