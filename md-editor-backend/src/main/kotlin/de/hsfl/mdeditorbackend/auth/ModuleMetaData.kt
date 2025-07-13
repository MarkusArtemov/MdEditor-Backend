package de.hsfl.mdeditorbackend.auth

import org.springframework.modulith.ApplicationModule
import org.springframework.modulith.PackageInfo

@ApplicationModule(
    id = "auth",
    displayName = "Authentication Module",
    allowedDependencies = ["common::api"]
)
@PackageInfo
class ModuleMetaData() {}
