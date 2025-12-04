import request from '@/api'

// 从指定源URL爬取音乐
export const crawlMusicFromSource = (sourceUrl: string) => {
  return request<string[]>({
    url: '/api/v1/crawler/crawl',
    method: 'post',
    params: {
      sourceUrl
    }
  })
}

// 调度爬取任务
export const scheduleCrawlingJob = (cronExpression: string) => {
  return request<string>({
    url: '/api/v1/crawler/schedule',
    method: 'post',
    params: {
      cronExpression
    }
  })
}

// 执行每日爬取任务
export const executeDailyCrawlingJob = () => {
  return request<string>({
    url: '/api/v1/crawler/daily-crawl',
    method: 'post'
  })
}
