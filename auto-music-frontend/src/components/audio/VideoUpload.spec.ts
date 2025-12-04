import { describe, it, expect, vi, beforeEach } from 'vitest'
import { mount } from '@vue/test-utils'
import VideoUpload from './VideoUpload.vue'
import { uploadVideo, extractAudioFromUrl } from '@/api/modules/crawler'
import { ElMessage } from 'element-plus'

// Mock API calls
vi.mock('@/api/modules/crawler', () => ({
  uploadVideo: vi.fn(),
  extractAudioFromUrl: vi.fn()
}))

// Mock ElMessage
vi.mock('element-plus', () => ({
  ElMessage: {
    success: vi.fn(),
    error: vi.fn()
  }
}))

describe('VideoUpload.vue', () => {
  beforeEach(() => {
    // Reset mocks
    vi.clearAllMocks()
  })

  it('renders correctly', () => {
    const wrapper = mount(VideoUpload)
    expect(wrapper.find('.video-upload').exists()).toBe(true)
    expect(wrapper.find('.high-tech-title').text()).toBe('视频音频提取')
  })

  it('switches between file upload and URL tabs', async () => {
    const wrapper = mount(VideoUpload)
    
    // Check initial tab
    expect(wrapper.find('.upload-section').exists()).toBe(true)
    expect(wrapper.find('.url-section').exists()).toBe(false)
    
    // Switch to URL tab
    await wrapper.find('.tab-button:nth-child(2)').trigger('click')
    expect(wrapper.find('.upload-section').exists()).toBe(false)
    expect(wrapper.find('.url-section').exists()).toBe(true)
    
    // Switch back to file tab
    await wrapper.find('.tab-button:nth-child(1)').trigger('click')
    expect(wrapper.find('.upload-section').exists()).toBe(true)
    expect(wrapper.find('.url-section').exists()).toBe(false)
  })

  it('shows error message when no file is selected', async () => {
    const wrapper = mount(VideoUpload)
    
    // Click upload button without selecting a file
    await wrapper.find('.high-tech-button').trigger('click')
    
    // Check error message
    expect(ElMessage.error).toHaveBeenCalledWith('请选择视频文件')
  })

  it('shows error message when no URL is entered', async () => {
    const wrapper = mount(VideoUpload)
    
    // Switch to URL tab
    await wrapper.find('.tab-button:nth-child(2)').trigger('click')
    
    // Click extract button without entering a URL
    await wrapper.find('.high-tech-button').trigger('click')
    
    // Check error message
    expect(ElMessage.error).toHaveBeenCalledWith('请输入视频URL')
  })

  it('calls uploadVideo API when file is selected', async () => {
    const wrapper = mount(VideoUpload)
    const mockFile = new File(['test'], 'test.mp4', { type: 'video/mp4' })
    
    // Mock API response
    (uploadVideo as vi.Mock).mockResolvedValue({
      code: 200,
      data: {
        title: 'Test Music',
        artist: 'Test Artist',
        duration: 120,
        quality: 'high'
      }
    })
    
    // Select file
    await wrapper.find('input[type="file"]').trigger('change', { target: { files: [mockFile] } })
    
    // Click upload button
    await wrapper.find('.high-tech-button').trigger('click')
    
    // Check API call
    expect(uploadVideo).toHaveBeenCalled()
    expect(ElMessage.success).toHaveBeenCalledWith('音频提取成功')
  })

  it('calls extractAudioFromUrl API when URL is entered', async () => {
    const wrapper = mount(VideoUpload)
    const mockUrl = 'https://example.com/video.mp4'
    
    // Mock API response
    (extractAudioFromUrl as vi.Mock).mockResolvedValue({
      code: 200,
      data: {
        title: 'Test Music',
        artist: 'Test Artist',
        duration: 120,
        quality: 'high'
      }
    })
    
    // Switch to URL tab
    await wrapper.find('.tab-button:nth-child(2)').trigger('click')
    
    // Enter URL
    await wrapper.find('input[type="text"]').setValue(mockUrl)
    
    // Click extract button
    await wrapper.find('.high-tech-button').trigger('click')
    
    // Check API call
    expect(extractAudioFromUrl).toHaveBeenCalledWith(mockUrl, expect.any(Function))
    expect(ElMessage.success).toHaveBeenCalledWith('音频提取成功')
  })
})