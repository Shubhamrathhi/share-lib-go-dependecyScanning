def call() {
    node {
        // Create an instance of GoUtils and pass pipeline context
        def goUtils = new org.example.utils.GoUtils(this)

        stage('Checkout') {
            checkoutSCM()
        }

        stage('Install Go (ARM64)') {
            goUtils.installGoARM64()
        }

        stage('Setup Go Dependencies') {
            goUtils.setupDependencies()
        }

        stage('Install GoSec (ARM64)') {
            goUtils.installGoSec()
        }

        stage('Run GoSec Scan') {
            goUtils.runGoSecScan()
        }

        stage('Archive Report') {
            goUtils.archiveReport()
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
