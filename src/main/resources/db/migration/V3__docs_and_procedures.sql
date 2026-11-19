-- ==============================================================
-- V3: CREATE PROCEDURE_DOCS TABLE & SEED REAL STEP-BY-STEP GUIDES
-- ==============================================================

CREATE TABLE IF NOT EXISTS procedure_docs (
    id BIGSERIAL PRIMARY KEY,
    slug VARCHAR(120) UNIQUE NOT NULL,
    title VARCHAR(255) NOT NULL,
    summary TEXT,
    file_name VARCHAR(255),
    file_url VARCHAR(500),
    tags TEXT,
    steps_json TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO procedure_docs (id, slug, title, summary, file_name, file_url, tags, steps_json)
VALUES
(
    1,
    'hoc-bong-khuyen-khich-hoc-tap',
    'Hướng dẫn đăng ký xét Học bổng Khuyến khích Học tập',
    'Quy trình sinh viên đăng ký và đối soát xét học bổng KKHT theo từng học kỳ tại Trường Đại học Bách khoa - ĐHĐN.',
    'mau-don-dang-ky-hoc-bong-kkht.doc',
    '/api/docs/1/download',
    '["Học bổng","Biểu mẫu","Công tác sinh viên"]',
    '[{"order":1,"title":"Kiểm tra điều kiện","description":"Đối chiếu điểm trung bình học kỳ (GPA) và điểm rèn luyện với bảng tiêu chuẩn xét học bổng theo QĐ 1414/QĐ-ĐHBK."},{"order":2,"title":"Tải và điền biểu mẫu","description":"Tải biểu mẫu đề nghị/đối soát ở nút tải bên cạnh, điền đầy đủ thông tin cá nhân và kết quả học tập."},{"order":3,"title":"Xin xác nhận của Khoa","description":"Nộp biểu mẫu cho Giáo vụ khoa để xác nhận kết quả học tập và điểm rèn luyện (nếu có sai lệch)."},{"order":4,"title":"Nộp hồ sơ tại Một cửa","description":"Nộp hồ sơ tại Phòng Công tác Sinh viên (bộ phận Một cửa) hoặc gửi phản hồi trực tuyến trước hạn quy định."},{"order":5,"title":"Theo dõi kết quả","description":"Kết quả xét học bổng chính thức được công bố trên cổng thông tin sinh viên sv.dut.udn.vn sau 10-15 ngày làm việc."}]'
),
(
    2,
    'giay-xac-nhan-sinh-vien',
    'Thủ tục xin cấp Giấy xác nhận sinh viên',
    'Dùng để bổ túc hồ sơ vay vốn ngân hàng, tạm hoãn nghĩa vụ quân sự, đăng ký tạm trú hoặc xin thực tập.',
    'mau-giay-xac-nhan-sinh-vien.doc',
    '/api/docs/2/download',
    '["Thủ tục một cửa","Giấy xác nhận","Biểu mẫu"]',
    '[{"order":1,"title":"Tải biểu mẫu","description":"Tải mẫu giấy xác nhận sinh viên và chọn đúng mục đích xác nhận (NVQS, vay vốn, thực tập...)."},{"order":2,"title":"Điền thông tin","description":"Điền họ tên, mã số sinh viên, lớp, khoa, năm học và lý do xin xác nhận."},{"order":3,"title":"Nộp tại bộ phận Một cửa","description":"Nộp giấy kèm thẻ sinh viên tại bộ phận Một cửa - Phòng Công tác Sinh viên trong giờ hành chính."},{"order":4,"title":"Nhận kết quả","description":"Nhận giấy xác nhận đã ký, đóng dấu sau 1-2 ngày làm việc."}]'
),
(
    3,
    'phuc-khao-diem-thi',
    'Quy trình đăng ký phúc khảo điểm thi kết thúc học phần',
    'Áp dụng khi sinh viên cho rằng điểm thi kết thúc học phần chưa phản ánh đúng bài làm.',
    'mau-don-phuc-khao-diem-thi.doc',
    '/api/docs/3/download',
    '["Học vụ","Phúc khảo","Điểm thi"]',
    '[{"order":1,"title":"Kiểm tra thời hạn","description":"Đơn phúc khảo chỉ được tiếp nhận trong vòng 7 ngày kể từ ngày công bố điểm học phần."},{"order":2,"title":"Điền đơn phúc khảo","description":"Tải mẫu đơn, ghi rõ mã học phần, lớp học phần, ngày thi và điểm đã công bố."},{"order":3,"title":"Nộp đơn và lệ phí","description":"Nộp đơn tại Văn phòng khoa quản lý học phần hoặc Phòng Khảo thí và đóng lệ phí phúc khảo theo quy định."},{"order":4,"title":"Chấm phúc khảo","description":"Khoa tổ chức chấm lại bài thi bởi hai giảng viên khác với lần chấm đầu."},{"order":5,"title":"Nhận kết quả","description":"Điểm sau phúc khảo được cập nhật trên hệ thống sinh viên trong vòng 10 ngày làm việc."}]'
),
(
    4,
    'nghi-hoc-tam-thoi-bao-luu',
    'Thủ tục xin nghỉ học tạm thời và bảo lưu kết quả học tập',
    'Dành cho sinh viên cần tạm dừng học vì lý do sức khỏe, nghĩa vụ quân sự hoặc hoàn cảnh gia đình.',
    'mau-don-xin-bao-luu.doc',
    '/api/docs/4/download',
    '["Học vụ","Bảo lưu","Biểu mẫu"]',
    '[{"order":1,"title":"Kiểm tra điều kiện","description":"Sinh viên đã học ít nhất một học kỳ, không bị buộc thôi học hoặc đang bị xem xét kỷ luật."},{"order":2,"title":"Chuẩn bị hồ sơ","description":"Điền đơn xin nghỉ học tạm thời kèm minh chứng (giấy khám sức khỏe, lệnh gọi nhập ngũ, xác nhận địa phương...)."},{"order":3,"title":"Xin ý kiến của Khoa","description":"Trình đơn cho Cố vấn học tập và Trưởng khoa ký xác nhận."},{"order":4,"title":"Nộp tại Phòng Đào tạo","description":"Nộp hồ sơ tại Phòng Đào tạo trước khi bắt đầu học kỳ hoặc trong 2 tuần đầu học kỳ."},{"order":5,"title":"Nhận quyết định","description":"Nhận Quyết định cho nghỉ học tạm thời sau 5-7 ngày làm việc."},{"order":6,"title":"Làm thủ tục nhập học lại","description":"Trước khi hết thời gian bảo lưu, nộp đơn xin tiếp tục học ít nhất 1 tuần trước học kỳ mới."}]'
),
(
    5,
    'dang-ky-ky-tuc-xa',
    'Hướng dẫn đăng ký ở Ký túc xá sinh viên',
    'Quy trình đăng ký chỗ ở nội trú tại Ký túc xá Trường ĐHBK - ĐHĐN theo từng năm học.',
    'mau-don-dang-ky-ky-tuc-xa.doc',
    '/api/docs/5/download',
    '["Ký túc xá","Công tác sinh viên","Biểu mẫu"]',
    '[{"order":1,"title":"Theo dõi thông báo","description":"Xem thông báo nhận hồ sơ nội trú đầu mỗi năm học trên trang thông tin của Trường và Ban Quản lý KTX."},{"order":2,"title":"Điền đơn đăng ký","description":"Tải mẫu đơn, điền thông tin cá nhân và đối tượng ưu tiên (nếu có)."},{"order":3,"title":"Nộp hồ sơ","description":"Nộp đơn kèm bản sao thẻ sinh viên, CCCD và giấy tờ ưu tiên tại Ban quản lý Ký túc xá."},{"order":4,"title":"Đóng phí và nhận phòng","description":"Sau khi có danh sách xét duyệt, đóng phí nội trú và nhận phòng theo lịch của Ban quản lý."}]'
)
ON CONFLICT (slug) DO NOTHING;

SELECT setval(pg_get_serial_sequence('procedure_docs', 'id'), COALESCE((SELECT MAX(id) FROM procedure_docs), 1), true);
