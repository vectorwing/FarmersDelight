# Farmer's Delight - 非官方 NeoForge 26.1.2 移植版

> **非官方社区移植版。** 本项目基于
> [Farmer's Delight](https://github.com/vectorwing/FarmersDelight) 修改，
> 原项目作者为 **vectorwing**。本项目与 vectorwing 或 Farmer's Delight
> 官方项目不存在隶属、维护、合作或背书关系。

本仓库将 Farmer's Delight 1.3.2 适配到 Minecraft 26.1.2 和 NeoForge
26.1.2。为了保持世界存档和附属模组兼容，本移植版保留原有的
`farmersdelight` mod id。


## 署名

- 原作者：**vectorwing**
- 原项目：<https://github.com/vectorwing/FarmersDelight>
- 原许可证：[MIT License](LICENSE)
- 移植身份：**非官方移植维护者**
- 移植维护者：**Alkaid-707-404**
- 移植源码：<https://github.com/Alkaid-707-404/FarmersDelight_NeoForge26.1.2>
- 问题反馈：<https://github.com/Alkaid-707-404/FarmersDelight_NeoForge26.1.2/issues>

本项目完整保留原版权声明和 MIT License，原项目贡献者仍保留在模组元数据
中，维护本移植版不代表取得原模组作者身份。

## 安装方法

1. 安装 Minecraft 26.1.2。
2. 安装 NeoForge 26.1.2。当前构建使用的 NeoForge 版本为
   `26.1.2.80`。
3. 下载本移植版 jar，例如
   `FarmersDelight-26.1.2-1.3.2-port.12.jar`。
4. 将 jar 放入对应实例的 `mods` 文件夹。
5. 启动游戏，确认模组列表中出现
   `Farmer's Delight - Unofficial NeoForge 26.1.2 Port`。

本移植版本体不要求额外前置模组。JEI、EMI、AppleSkin、CraftTweaker 等可选
联动目前尚未完成 26.1.2 适配，因此不作为必需依赖。

## 当前限制

- JEI、EMI、AppleSkin、CraftTweaker 等可选联动源码暂时未启用。
- 数据生成器源码暂时未完整迁移；当前发布包直接携带已经可用的数据资源。
- 部分村民事件、旧版渲染 hook 和其他非核心系统仍可能需要后续适配。
- 多人服务器、复杂整合包、附属模组兼容和旧世界升级仍需要更大范围测试。

## 再分发和支持

- 不要将本项目描述为官方新版、官方更新或官方发布。
- 与本移植版相关的问题请提交到移植仓库，不要提交到上游 Farmer's Delight
  问题反馈区。
- 只有在许可证和再分发条款清楚并被保留时，才可以加入第三方素材或依赖。
