# Development Worklog

## Current Decisions

- 共享契约以 `docs/api-contract.md` 为准
- SSE 事件以 `docs/sse-events.md` 为准
- `projectId` 使用 `proj_YYYYMMDD_xxx` 格式
- 数据库、后端接口和前端路由统一只使用字符串 `projectId`，不再使用数字项目 ID
- `sceneId` 使用 `S001` 递增格式
- 空数组统一返回 `[]`

## A Line Status

- 已完成项目创建接口
- 已完成项目列表查询接口
- 已完成原文提交和章节切分接口
- 已完成章节摘要字段与摘要生成接口
- 已完成基础后端工程骨架
- 已完成 AI 优先、规则兜底的章节摘要与角色/地点/事件中间资产生成
- 已完成 AI 优先、规则兜底的场景大纲和 Scene 详情接口
- 已完成本地前后端跨域配置
- 当前真实可用接口：`POST /api/projects`、`GET /api/projects`、`GET /api/projects/{projectId}`、`POST /api/projects/{projectId}/source`、`GET /api/projects/{projectId}/chapters`
- 当前 A 线新增接口：`POST /api/projects/{projectId}/chapters/summarize`、`POST /api/projects/{projectId}/analyze`、`GET /api/projects/{projectId}/entities`、`GET /api/projects/{projectId}/story-events`、`GET /api/projects/{projectId}/outline`、`GET /api/projects/{projectId}/scenes`、`GET /api/projects/{projectId}/scenes/{sceneId}`、`POST /api/projects/{projectId}/scenes/{sceneId}/regenerate`

## B Line Status

- 已完成前端工作台骨架
- 已完成 mock 场景工作台
- 已完成真实项目引导流
- 已完成真实项目创建和项目列表接入
- 已完成真实小说正文提交流程
- 已完成真实章节面板接入
- 已完成真实故事资产分析触发
- 已完成真实角色/地点面板接入
- 已完成真实故事事件面板接入
- 当前工作台状态：`真实项目管理/正文提交/章节/实体/事件/场景大纲/Scene 详情 + mock 校验/YAML`

## Next A Line Dependencies

- 完善 `validate`、`export` 和 SSE 进度流接口
- 根据联调结果细化 AI 提示词和错误处理
- `GET /api/projects/{projectId}/events` 保留给 SSE，不用于故事事件列表

## Next B Line Tasks

- 联调真实 `outline` 和 `scene detail`
- 用真实 `export` 替换 YAML 预览 mock
- 用真实 `events` 替换当前阶段进度静态状态
- 接入真实 `validate` 和导出交互
- 保持 mock 回退能力，避免 A 线未完成时阻塞演示

## Breaking Changes

- 暂无
