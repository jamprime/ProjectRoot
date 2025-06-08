import os
from pathlib import Path

def get_file_contents(file_path, max_lines=None, max_file_size=1000000):
    """Get file contents with line numbers, respecting size limits."""
    try:
        # Check file size
        file_size = os.path.getsize(file_path)
        if file_size > max_file_size:
            return f"[File too large to display. Size: {file_size / 1024:.1f} KB]"
        
        with open(file_path, 'r', encoding='utf-8') as f:
            lines = f.readlines()
            
            # If max_lines is specified and file is longer, show first and last parts
            if max_lines and len(lines) > max_lines:
                content = []
                content.extend(f"{i+1:4d}: {line.rstrip()}\n" for i, line in enumerate(lines[:max_lines//2]))
                content.append(f"{'...':4s} {len(lines) - max_lines} lines omitted ...\n")
                content.extend(f"{i+1:4d}: {line.rstrip()}\n" for i, line in enumerate(lines[-max_lines//2:], start=len(lines)-max_lines//2))
                return ''.join(content)
            else:
                return ''.join(f"{i+1:4d}: {line.rstrip()}\n" for i, line in enumerate(lines))
    except Exception as e:
        return f"[Error reading file: {str(e)}]"

def get_project_structure(startpath, output_file, exclude_dirs=None, include_contents=True, 
                         max_lines=None, max_file_size=1000000, exclude_files=None):
    if exclude_dirs is None:
        exclude_dirs = ['__pycache__']
    if exclude_files is None:
        exclude_files = ['.git', '.env', 'project_structure.txt']
    
    with open(output_file, 'w', encoding='utf-8') as f:
        for root, dirs, files in os.walk(startpath):
            # Skip excluded directories
            dirs[:] = [d for d in dirs if d not in exclude_dirs]
            
            # Calculate the level of indentation
            level = root.replace(startpath, '').count(os.sep)
            indent = '│   ' * level
            
            # Write the current directory
            f.write(f"{indent}├── {os.path.basename(root)}/\n")
            
            # Write files in the current directory
            sub_indent = '│   ' * (level + 1)
            for file in files:
                if file not in exclude_files:
                    f.write(f"{sub_indent}├── {file}\n")
                    
                    # If include_contents is True, add file contents
                    if include_contents:
                        file_path = os.path.join(root, file)
                        contents = get_file_contents(file_path, max_lines, max_file_size)
                        content_indent = '│   ' * (level + 2)
                        f.write(f"{content_indent}└── Contents:\n")
                        for line in contents.split('\n'):
                            f.write(f"{content_indent}    {line}\n")

if __name__ == "__main__":
    # Get the current directory (project root)
    project_root = os.getcwd()
    output_file = "project_structure.txt"
    
    print(f"Exporting project structure to {output_file}...")
    get_project_structure(
        project_root, 
        output_file,
        exclude_dirs=['__pycache__', '.git', 'venv'],
        include_contents=True,
        max_lines=None,  # Show all lines by default
        max_file_size=1000000,  # 1MB maximum file size
        exclude_files=['export_structure.py', 'project_structure.txt']  # Files to exclude from content display
    )
    print("Done!") 