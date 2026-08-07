import SwiftUI

@main
struct iOSApp: App {
    init() {
        KoinModuleKt.doInitKoin(appDeclaration: { _ in })
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
