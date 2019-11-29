require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = package["name"]
  s.version      = package["version"]
  s.summary      = package["description"]
  s.description  = package["description"]
  s.homepage     = package["homepage"]
  s.license      = package["license"]
  s.authors      = { package["author"]["name"] => package["author"]["email"] }
  s.platform      = :ios, '8.0'
  s.source       = { :git => package["repository"]["url"] }

  s.source_files = "ios/**/*.{h,m,swift}"

  s.frameworks = 'CoreGraphics', 'SystemConfiguration', 'AVFoundation', 'CoreTelephony'
  s.libraries = 'sqlite3', 'c++', 'z'

  s.dependency "React"

  # 友盟基础依赖库
  s.dependency 'UMCCommon', "~> 2.1.1"
  s.dependency 'UMCSecurityPlugins', "~> 1.0.6"

  # U-Share SDK UI模块（分享面板，建议添加）
  s.dependency 'UMCShare/UI', "~> 6.9.6"

  # 集成微信(精简版0.2M)
  s.dependency 'UMCShare/Social/ReducedWeChat', "~> 6.9.6"

  # 集成QQ/QZone/TIM(精简版0.5M)
  s.dependency 'UMCShare/Social/ReducedQQ', "~> 6.9.6"

  # UMCPush
  s.dependency 'UMCPush', "~> 3.2.4"


  # UMCAnalytics 数据统计模块
  s.dependency 'UMCAnalytics', "~> 6.0.5"

  # UMCCommonLog
  s.dependency 'UMCCommonLog', "~> 1.0.0"

end

