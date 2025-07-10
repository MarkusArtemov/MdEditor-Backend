package de.hsfl.mdeditorbackend.user

import org.springframework.modulith.ApplicationModule
import org.springframework.modulith.PackageInfo

@ApplicationModule(
    id = "user",
    displayName = "User Profile Module",
    allowedDependencies = ["auth"],
)
@PackageInfo
class ModuleMetaData() {}