@echo off
:begin
setlocal

pushd %~dp0

echo Building with ant...
call ant build-hfsxlib
if "%ERRORLEVEL%"=="0" (echo Done!) else echo Problems while building with ant... && goto error

popd
goto end

:error
echo There were errors...
goto end

:end
endlocal
